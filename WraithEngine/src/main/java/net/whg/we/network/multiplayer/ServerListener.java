package net.whg.we.network.multiplayer;

import net.whg.we.event.Listener;
import net.whg.we.network.netty.UserConnection;

public interface ServerListener extends Listener
{
	void onServerStarted(MultiplayerServer server);

	void onServerFailedToStart(MultiplayerServer server, int port);

	void onClientConnected(MultiplayerServer server, UserConnection client);

	void onClientDisconnected(MultiplayerServer server, UserConnection client);

	void onServerStopped(MultiplayerServer server);
}
