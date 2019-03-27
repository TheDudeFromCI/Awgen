package net.whg.we.network.multiplayer;

import net.whg.we.event.EventCallerBase;
import net.whg.we.network.netty.UserConnection;

public class ClientEvent extends EventCallerBase<ClientListener> implements ConnectionEvent
{
	private static final int CONNECT_TO_SERVER_EVENT = 0;
	private static final int DISCONNECTED_FROM_SERVER_EVENT = 1;

	private MultiplayerClient _client;

	public ClientEvent(MultiplayerClient client)
	{
		_client = client;
	}

	@Override
	public void onConnect(UserConnection con)
	{
		callEvent(CONNECT_TO_SERVER_EVENT, con);
	}

	@Override
	public void onDisconnect(UserConnection con)
	{
		callEvent(DISCONNECTED_FROM_SERVER_EVENT, con);
	}

	@Override
	protected void runEvent(ClientListener listener, int index, Object arg)
	{
		switch (index)
		{
			case CONNECT_TO_SERVER_EVENT:
				listener.onConnectToServer(_client, (UserConnection) arg);
				return;

			case DISCONNECTED_FROM_SERVER_EVENT:
				listener.onDisconnectedFromServer(_client, (UserConnection) arg);
				return;

			default:
				throw new IllegalArgumentException("Unknown event index! " + index);
		}
	}
}
