package net.whg.frameworks.network.multiplayer;

import net.whg.frameworks.event.EventCallerBase;

public class ClientEvent extends EventCallerBase<ClientListener>
{
	private static final int CONNECT_TO_SERVER_EVENT = 0;
	private static final int DISCONNECTED_FROM_SERVER_EVENT = 1;

	private MultiplayerClient _client;

	public ClientEvent(MultiplayerClient client)
	{
		_client = client;
	}

	public void onConnect()
	{
		callEvent(CONNECT_TO_SERVER_EVENT);
	}

	public void onDisconnect()
	{
		callEvent(DISCONNECTED_FROM_SERVER_EVENT);
	}

	@Override
	protected void runEvent(ClientListener listener, int index, Object arg)
	{
		switch (index)
		{
			case CONNECT_TO_SERVER_EVENT:
				listener.onConnectToServer(_client);
				return;

			case DISCONNECTED_FROM_SERVER_EVENT:
				listener.onDisconnectedFromServer(_client);
				return;

			default:
				throw new IllegalArgumentException("Unknown event index! " + index);
		}
	}
}
