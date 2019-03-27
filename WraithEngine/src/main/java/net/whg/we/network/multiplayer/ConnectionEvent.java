package net.whg.we.network.multiplayer;

import net.whg.we.network.netty.UserConnection;

public interface ConnectionEvent
{
	void onConnect(UserConnection connection);

	void onDisconnect(UserConnection connection);
}
