package net.whg.we.network.server;

import java.util.ArrayList;
import java.util.List;

import net.whg.we.utils.GenericRunnable;

public class ConnectedClientList
{
	private Object LOCK = new Object();
	private List<ClientConnection> _clients = new ArrayList<>();

	public void addClient(ClientConnection connection)
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

	public void removeClient(ClientConnection connection)
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

	public ClientConnection getClient(int index)
	{
		synchronized (LOCK)
		{
			return _clients.get(index);
		}
	}
}
