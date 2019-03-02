package net.whg.we.network.server;

import net.whg.we.network.Packet;

public interface ConnectionListener
{
	void onConnected(ClientConnection connection);

	void onOutgoingPacket(ClientConnection connection, Packet packet);

	void onIncomingPacket(ClientConnection connection, Packet packet, String typePath,
			int readBytes);

	void onDisconnected(ClientConnection connection);
}
