package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

class ClientConnectionThread
{
	private Object LOCK = new Object();
	private TCPChannel _socket;
	private Thread _thread;
	private boolean _closed;
	private ChannelProtocol _protocol;

	ClientConnectionThread(TCPChannel socket, ChannelProtocol protocol) throws IOException
	{
		_socket = socket;
		_protocol = protocol;
		_protocol.init(socket.getInputStream(), socket.getOutputStream());

		synchronized (LOCK)
		{
			_thread = new Thread(() ->
			{
				try
				{
					while (true)
						_protocol.next();
				}
				catch (IOException e)
				{
					Log.errorf("An error has occured within the client connection!", e);
					close();
				}

				_protocol.onDisconnected();
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
				_protocol.close();
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
		}
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}
}
