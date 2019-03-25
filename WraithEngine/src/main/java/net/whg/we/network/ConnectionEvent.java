package net.whg.we.network;

public interface ConnectionEvent
{
	void onConnect(Connection connection);

	void onDisconnect(Connection connection);
}
