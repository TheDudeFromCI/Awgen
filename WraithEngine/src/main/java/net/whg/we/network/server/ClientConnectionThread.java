package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class ClientConnectionThread
{
	private TCPChannel _socket;
	private Thread _thread;
	private ChannelProtocol _protocol;

	public ClientConnectionThread(TCPChannel socket, ChannelProtocol protocol) throws IOException
	{
		_socket = socket;
		_protocol = protocol;
		_protocol.init(socket.getInputStream(), socket.getOutputStream());

		_thread = new Thread(new ClientProtocolHandler(socket, protocol));
		_thread.setDaemon(true);
		_thread.start();
	}

	public boolean isClosed()
	{
		return _socket.isClosed();
	}

	public void close()
	{
		Log.info("Force closing socket connection.");

		if (isClosed())
			return;

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
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}
}
