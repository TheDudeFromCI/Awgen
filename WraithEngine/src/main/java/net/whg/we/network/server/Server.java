package net.whg.we.network.server;

public interface Server
{
	void startServer();

	void stopServer();

	boolean isRunning();

	int getPort();

	void setPort(int port);

	ConnectedClientList getClientList();
}
