package net.whg.we.network.server;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection
{
	private ClientConnectionThread _connection;

	public ClientConnection(Socket socket, ConnectionListener listener) throws IOException
	{
		_connection = new ClientConnectionThread(socket, this, listener);
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
