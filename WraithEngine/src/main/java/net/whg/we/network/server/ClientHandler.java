package net.whg.we.network.server;

import net.whg.we.network.Packet;
import net.whg.we.network.PacketFactory;

public class ClientHandler implements ConnectionListener
{
	private ConnectedClientList _clientList;
	private PacketFactory _packetFactory;

	public ClientHandler(ConnectedClientList clientList, PacketFactory packetFactory)
	{
		_clientList = clientList;
		_packetFactory = packetFactory;
	}

	@Override
	public void onConnected(ClientConnection connection)
	{
		_clientList.addClient(connection);
	}

	@Override
	public void onOutgoingPacket(ClientConnection connection, Packet packet)
	{
		packet.encode();
	}

	@Override
	public void onIncomingPacket(ClientConnection connection, Packet packet, String typePath,
			int readBytes)
	{
		packet.setPacketType(_packetFactory.findPacketType(typePath));
		packet.decode(readBytes);
	}

	@Override
	public void onDisconnected(ClientConnection connection)
	{
		_clientList.removeClient(connection);
	}
}
