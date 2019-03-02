package net.whg.we.network.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import net.whg.we.utils.logging.Log;

class ClientConnectionThread
{
	private static final int MAX_PACKET_SIZE = 4096;

	private Object LOCK = new Object();
	private Socket _socket;
	private Thread _thread;
	private boolean _closed;
	private ClientConnection _client;
	private ConnectionListener _listener;
	private BufferedOutputStream _out;

	ClientConnectionThread(Socket socket, ClientConnection client, ConnectionListener listener)
			throws IOException
	{
		_socket = socket;
		_client = client;
		_listener = listener;

		synchronized (LOCK)
		{
			_out = new BufferedOutputStream(_socket.getOutputStream());

			_thread = new Thread(() ->
			{
				try (BufferedInputStream in = new BufferedInputStream(_socket.getInputStream()))
				{
					byte[] buffer = new byte[MAX_PACKET_SIZE];

					while (true)
					{
						int packetSize = (in.read() & 0xFF) << 8;
						packetSize |= in.read() & 0xFF;
						in.read(buffer, 0, packetSize);

						// TODO Handle input packet
					}
				}
				catch (IOException e)
				{
					Log.errorf("An error has occured within the client connection!", e);
					close();
				}

				_listener.onDisconnected(_client);
			});

			_thread.setDaemon(true);
			_thread.start();
		}
	}

	public boolean isClosed()
	{
		synchronized (LOCK)
		{
			return _closed;
		}
	}

	public void close()
	{
		Log.info("Force closing socket connection.");

		synchronized (LOCK)
		{
			if (_closed)
				return;

			_closed = true;

			try
			{
				_socket.close();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to close socket!", e);
			}

			try
			{
				_thread.join();
			}
			catch (InterruptedException e)
			{
				Log.errorf("Failed to wait for client connection thread to end!", e);
			}

			_socket = null;
			_thread = null;
			_out = null;
			_client = null;
			_listener = null;
		}
	}

	public void send(byte[] data, int pos, int length)
	{
		if (length > MAX_PACKET_SIZE)
			throw new IllegalArgumentException("Packets may not exceed maximum packet size!");

		synchronized (LOCK)
		{
			if (isClosed())
				return;

			try
			{
				_out.write(length >> 8 & 0xFF);
				_out.write(length & 0xFF);
				_out.write(data, pos, length);
				_out.flush();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to write to output stream!", e);
				close();
			}
		}
	}
}
