package net.whg.we.network.multiplayer;

import net.whg.frameworks.event.Listener;

public interface ClientListener extends Listener
{
	void onConnectToServer(MultiplayerClient client);

	void onDisconnectedFromServer(MultiplayerClient client);
}
