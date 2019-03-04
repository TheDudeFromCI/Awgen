package net.whg.we.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import net.whg.we.network.DefaultTCPChannel;
import net.whg.we.network.TCPChannel;

public class DefaultTCPSocket implements TCPSocket
{
	private ServerSocket _serverSocket;

	@Override
	public void close() throws IOException
	{
		try
		{
			_serverSocket.close();
		}
		finally
		{
			_serverSocket = null;
		}
	}

	@Override
	public void open(int port) throws IOException
	{
		if (_serverSocket != null)
			throw new IllegalStateException("Server socket already open!");

		_serverSocket = new ServerSocket(port);
	}

	@Override
	public TCPChannel nextChannel() throws IOException
	{
		return new DefaultTCPChannel(_serverSocket.accept(), false);
	}

}
