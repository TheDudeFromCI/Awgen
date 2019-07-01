package net.whg.frameworks.network.multiplayer;

import net.whg.frameworks.logging.Log;

public class MultiplayerClientListener implements ClientListener
{
	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void onConnectToServer(MultiplayerClient client)
	{
		Log.debugf("Opened connection to server.");
	}

	@Override
	public void onDisconnectedFromServer(MultiplayerClient client)
	{
		Log.debugf("Closed connection to server.");
	}
}
