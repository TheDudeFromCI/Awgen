package net.whg.we.network.packet;

public class PacketManager
{
	private PacketPool _packetPool;
	private PacketFactory _packetFactory;

	public PacketManager(PacketPool packetPool, PacketFactory packetFactory)
	{
		_packetPool = packetPool;
		_packetFactory = packetFactory;
	}

	public PacketPool pool()
	{
		return _packetPool;
	}

	public PacketFactory factory()
	{
		return _packetFactory;
	}

	public Packet newPacket(String type)
	{
		Packet packet = _packetPool.get();
		packet.setPacketType(_packetFactory.findPacketType(type));
		return packet;
	}
}
