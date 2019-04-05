package net.whg.we.network.multiplayer;

import net.whg.frameworks.logging.Log;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.scene.ServerGameState;

public class MultiplayerServerListener implements ServerListener
{
	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void onServerStarted(ServerGameState gameState)
	{
		Log.info("Server successfully started.");
	}

	@Override
	public void onServerFailedToStart(ServerGameState gameState, int port)
	{
		Log.warnf("Failed to start server on port %d!", port);
	}

	@Override
	public void onClientConnected(ServerGameState gameState, UserConnection client)
	{
		Log.debugf("Adding client to pending connection list. IP: %s", client.getIP());
	}

	@Override
	public void onClientDisconnected(ServerGameState gameState, UserConnection client)
	{
		Log.debugf("Client %s has disconnected.", client.getIP());

		if (client.getUserState().isAuthenticated())
			gameState.getPlayerList().removePlayer(
					gameState.getPlayerList().getPlayerByToken(client.getUserState().getToken()));
	}

	@Override
	public void onServerStopped(ServerGameState gameState)
	{
		gameState.getPlayerList().clear();
	}
}
