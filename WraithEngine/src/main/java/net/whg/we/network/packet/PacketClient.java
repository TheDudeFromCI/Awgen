package net.whg.we.network.packet;

import java.io.IOException;
import net.whg.we.network.client.DefaultClient;

public class PacketClient extends DefaultClient
{
	private static PacketProtocol buildPacketProtocol(PacketFactory factory, PacketHandler handler)
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool, handler);
		return new PacketProtocol(pool, factory, processor);
	}

	private PacketPool _pool;
	private PacketProtocol _protocol;
	private PacketProcessor _processor;
	private PacketFactory _factory;

	public PacketClient(PacketFactory packetFactory, PacketHandler handler, String ip, int port)
			throws IOException
	{
		super(buildPacketProtocol(packetFactory, handler), ip, port);

		_protocol = (PacketProtocol) getProtocol();
		_pool = _protocol.getPacketPool();
		_processor = (PacketProcessor) _protocol.getListener();
		_factory = _protocol.getPacketFactory();
	}

	public PacketProcessor getPacketProcessor()
	{
		return _processor;
	}

	public void handlePackets()
	{
		_processor.handlePackets();
	}

	public void sendPacket(Packet packet)
	{
		_protocol.sendPacket(packet);
	}

	public Packet newPacket(String typePath)
	{
		Packet packet = _pool.get();
		packet.setPacketType(_factory.findPacketType(typePath));

		return packet;
	}
}
