package net.whg.we.network.multiplayer;

import java.util.ArrayList;
import net.whg.we.network.Connection;
import net.whg.we.network.IPAddress;

public class PendingClients
{
	private ArrayList<HandshakeTimeout> _pending = new ArrayList<>();

	public void update()
	{
		for (int i = 0; i < _pending.size();)
		{
			HandshakeTimeout timeout = _pending.get(i);

			if (timeout.isTimeUp())
			{
				timeout.getClient().close();
				_pending.remove(i);
				continue;
			}

			i++;
		}
	}

	public void addClient(Connection client)
	{
		_pending.add(new HandshakeTimeout(client, HandshakeTimeout.DEFAULT_CONNECT_TIME));
	}

	public void removeClient(Connection client)
	{
		for (int i = 0; i < _pending.size(); i++)
		{
			if (_pending.get(i).getClient() == client)
			{
				_pending.remove(i);
				return;
			}
		}
	}

	public void clear()
	{
		_pending.clear();
	}

	public Connection getClient(IPAddress ip)
	{
		for (HandshakeTimeout timeout : _pending)
			if (timeout.getClient().getIP().equals(ip))
				return timeout.getClient();
		return null;
	}
}
