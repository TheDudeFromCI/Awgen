package net.whg.we.network.multiplayer;

import net.whg.we.event.Listener;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.scene.ServerGameState;

public interface ServerListener extends Listener
{
	void onServerStarted(ServerGameState gameState);

	void onServerFailedToStart(ServerGameState gameState, int port);

	void onClientConnected(ServerGameState gameState, UserConnection client);

	void onClientDisconnected(ServerGameState gameState, UserConnection client);

	void onServerStopped(ServerGameState gameState);
}
