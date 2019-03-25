package net.whg.we.network.server;

import net.whg.we.event.Listener;
import net.whg.we.network.Connection;

public interface ServerListener extends Listener
{
	void onServerStarted(Server server);

	void onServerFailedToStart(Server server, int port);

	void onClientConnected(Server server, Connection client);

	void onClientDisconnected(Server server, Connection client);

	void onServerStopped(Server server);
}
