package net.whg.we.network.server;

public interface ConnectionListener
{
	void onConnected(ClientConnection connection);

	void onOutgoingPacket(ClientConnection connection, byte[] bytes, int pos, int length);

	void onIncomingPacket(ClientConnection connection, byte[] bytes, int length);

	void onDisconnected(ClientConnection connection);
}
