package net.whg.we.network.client;

import net.whg.we.event.EventCallerBase;
import net.whg.we.network.Connection;
import net.whg.we.network.ConnectionEvent;
import net.whg.we.network.TCPChannel;

public class ClientEvent extends EventCallerBase<ClientListener> implements ConnectionEvent
{
	private static final int CONNECT_TO_SERVER_EVENT = 0;
	private static final int DISCONNECTED_FROM_SERVER_EVENT = 1;

	private DefaultClient _client;

	public ClientEvent(DefaultClient client)
	{
		_client = client;
	}

	@Override
	public void onConnect(Connection con)
	{
		callEvent(CONNECT_TO_SERVER_EVENT, con.getChannel());
	}

	@Override
	public void onDisconnect(Connection con)
	{
		callEvent(DISCONNECTED_FROM_SERVER_EVENT, con.getChannel());
	}

	@Override
	protected void runEvent(ClientListener listener, int index, Object arg)
	{
		switch (index)
		{
			case CONNECT_TO_SERVER_EVENT:
				listener.onConnectToServer(_client, (TCPChannel) arg);
				return;

			case DISCONNECTED_FROM_SERVER_EVENT:
				listener.onDisconnectedFromServer(_client, (TCPChannel) arg);
				return;

			default:
				throw new IllegalArgumentException("Unknown event index! " + index);
		}
	}
}
