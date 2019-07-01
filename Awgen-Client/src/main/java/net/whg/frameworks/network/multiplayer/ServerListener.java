package net.whg.frameworks.network.multiplayer;

import net.whg.frameworks.event.Listener;
import net.whg.frameworks.network.netty.UserConnection;
import net.whg.we.legacy.ServerGameState;

public interface ServerListener extends Listener
{
	void onServerStarted(ServerGameState gameState);

	void onServerFailedToStart(ServerGameState gameState, int port);

	void onClientConnected(ServerGameState gameState, UserConnection client);

	void onClientDisconnected(ServerGameState gameState, UserConnection client);

	void onServerStopped(ServerGameState gameState);
}