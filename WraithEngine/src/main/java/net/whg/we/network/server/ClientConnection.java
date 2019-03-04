package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;

public class ClientConnection
{
	private ClientConnectionThread _connection;

	public ClientConnection(TCPChannel socket, ChannelProtocol protocol) throws IOException
	{
		_connection = new ClientConnectionThread(socket, protocol);
	}

	public boolean isClosed()
	{
		return _connection != null;
	}

	public void close()
	{
		if (isClosed())
			return;

		_connection.close();
		_connection = null;
	}
}
