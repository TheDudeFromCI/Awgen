package net.whg.we.network.server;

import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.PacketFactory;
import net.whg.we.network.PacketPool;
import net.whg.we.network.PacketProcessor;
import net.whg.we.network.PacketProtocol;
import net.whg.we.network.TCPChannel;

public class PacketServerProtocol implements ServerProtocol
{
	private PacketFactory _packetFactory;
	private PacketProcessor _packetProcessor;
	private PacketPool _packetPool;

	public PacketServerProtocol(PacketFactory packetFactory, PacketProcessor packetProcessor)
	{
		_packetFactory = packetFactory;
		_packetProcessor = packetProcessor;
		_packetPool = new PacketPool();
	}

	@Override
	public ChannelProtocol openChannelProtocol(TCPChannel channel)
	{
		return new PacketProtocol(_packetPool, _packetFactory, _packetProcessor, channel);
	}

}
