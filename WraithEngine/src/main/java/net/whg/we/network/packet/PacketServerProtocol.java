package net.whg.we.network.packet;

import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.server.Server;
import net.whg.we.network.server.ServerProtocol;

public class PacketServerProtocol implements ServerProtocol
{
	private PacketFactory _packetFactory;
	private PacketProcessor _packetProcessor;
	private PacketPool _packetPool;
	private Server _server;

	public PacketServerProtocol(Server server, PacketFactory packetFactory,
			PacketProcessor packetProcessor, PacketPool pool)
	{
		_server = server;
		_packetFactory = packetFactory;
		_packetProcessor = packetProcessor;
		_packetPool = pool;
	}

	public void setServer(Server server)
	{
		_server = server;
	}

	public Server getServer()
	{
		return _server;
	}

	@Override
	public ChannelProtocol createProtocolInstance()
	{
		return new PacketProtocol(_packetPool, _packetFactory, _packetProcessor);
	}

	public PacketPool getPacketPool()
	{
		return _packetPool;
	}

	public PacketFactory getPacketFactory()
	{
		return _packetFactory;
	}

	public PacketProcessor getPacketProcessor()
	{
		return _packetProcessor;
	}
}
