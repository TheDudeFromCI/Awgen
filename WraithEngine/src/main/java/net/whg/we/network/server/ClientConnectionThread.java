package net.whg.we.network.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import net.whg.we.network.Packet;
import net.whg.we.utils.logging.Log;

class ClientConnectionThread
{
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
					while (true)
					{
						int packetSize = (in.read() & 0xFF) << 8;
						packetSize |= in.read() & 0xFF;

						int packetTypeBytes = in.read() & 0xFF;
						byte[] nameBytes = new byte[packetTypeBytes];
						in.read(nameBytes);
						String name = new String(nameBytes, StandardCharsets.UTF_8);

						Packet packet = _client.getPacketPool().get();
						in.read(packet.getBytes(), 0, packetSize);

						_listener.onIncomingPacket(_client, packet, name, packetSize);
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

	public void send(Packet packet)
	{
		if (packet.getPacketType() == null)
		{
			Log.warn("Tried to send a packet with no packet type!");
			return;
		}

		byte[] nameBytes = packet.getPacketType().getTypePath().getBytes(StandardCharsets.UTF_8);
		if (nameBytes.length > 255)
			throw new IllegalArgumentException("Packet type name may not exceed 255 bytes!");

		synchronized (LOCK)
		{
			if (isClosed())
				return;

			try
			{
				int length = packet.encode();

				_out.write(length >> 8);
				_out.write(length);

				_out.write(nameBytes.length);
				_out.write(nameBytes);

				_out.write(packet.getBytes(), 0, length);
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
