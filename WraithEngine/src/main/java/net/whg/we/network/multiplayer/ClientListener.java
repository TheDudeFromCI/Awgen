package net.whg.we.network.multiplayer;

import net.whg.we.event.Listener;
import net.whg.we.network.netty.UserConnection;

public interface ClientListener extends Listener
{
	void onConnectToServer(MultiplayerClient client, UserConnection server);

	void onDisconnectedFromServer(MultiplayerClient client, UserConnection server);
}
