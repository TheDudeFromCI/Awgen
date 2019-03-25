package net.whg.we.network.packet;

import java.io.IOException;
import net.whg.we.network.Connection;
import net.whg.we.network.server.DefaultServer;
import net.whg.we.network.server.ServerProtocol;

public class PacketServer extends DefaultServer
{
	private static ServerProtocol buildPacketServerProtocol(PacketFactory factory,
			PacketHandler handler)
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool, handler);
		return new PacketServerProtocol(null, factory, processor, pool);
	}

	protected PacketServerProtocol _packetServerProtocol;

	public PacketServer(PacketFactory factory, PacketHandler handler, int port) throws IOException
	{
		super(buildPacketServerProtocol(factory, handler), port);
		_packetServerProtocol = (PacketServerProtocol) getProtocol();
		_packetServerProtocol.setServer(this);
	}

	public PacketPool getPacketPool()
	{
		return _packetServerProtocol.getPacketPool();
	}

	public PacketProcessor getPacketProcessor()
	{
		return _packetServerProtocol.getPacketProcessor();
	}

	public PacketFactory getPacketFactory()
	{
		return _packetServerProtocol.getPacketFactory();
	}

	public void handlePackets()
	{
		getPacketProcessor().handlePackets();
	}

	public Packet newPacket(String typePath)
	{
		Packet packet = getPacketPool().get();
		packet.setPacketType(getPacketFactory().findPacketType(typePath));

		return packet;
	}

	public void sendPacket(Packet packet, Connection client)
	{
		PacketProtocol protocol = (PacketProtocol) client.getProtocol();
		protocol.sendPacket(packet);
	}
}
