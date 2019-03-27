package net.whg.we.network.multiplayer;

import net.whg.we.network.netty.UserConnection;
import net.whg.we.utils.logging.Log;

public class MultiplayerServerListener implements ServerListener
{
	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void onServerStarted(MultiplayerServer server)
	{
		Log.info("Server successfully started.");
	}

	@Override
	public void onServerFailedToStart(MultiplayerServer server, int port)
	{
		Log.warnf("Failed to start server on port %d!", port);
	}

	@Override
	public void onClientConnected(MultiplayerServer server, UserConnection client)
	{
		Log.debugf("Adding client to pending connection list. IP: %s", client.getIP());
	}

	@Override
	public void onClientDisconnected(MultiplayerServer server, UserConnection client)
	{
		Log.debugf("Client %s has disconnected.", client.getIP());

		if (client.getUserState().isAuthenticated())
			server.getPlayerList().removePlayer(
					server.getPlayerList().getPlayerByToken(client.getUserState().getToken()));
	}

	@Override
	public void onServerStopped(MultiplayerServer server)
	{
		server.getPlayerList().clear();
	}
}
