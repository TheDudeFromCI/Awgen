package net.whg.we.network.server;

import java.util.ArrayList;
import java.util.List;
import net.whg.we.network.Connection;

public class ConnectedClientList
{
	private Object LOCK = new Object();
	private List<Connection> _clients = new ArrayList<>();

	public void addClient(Connection connection)
	{
		if (connection == null)
			return;

		synchronized (LOCK)
		{
			if (_clients.contains(connection))
				return;

			_clients.add(connection);
		}
	}

	public void removeClient(Connection connection)
	{
		if (connection == null)
			return;

		synchronized (LOCK)
		{
			_clients.remove(connection);
		}
	}

	public int getClientCount()
	{
		synchronized (LOCK)
		{
			return _clients.size();
		}
	}

	public Connection getClient(int index)
	{
		synchronized (LOCK)
		{
			return _clients.get(index);
		}
	}
}
