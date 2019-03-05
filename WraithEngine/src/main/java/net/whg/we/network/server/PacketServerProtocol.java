package net.whg.we.network.server;

import java.io.IOException;
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

	public PacketServerProtocol(PacketFactory packetFactory, PacketProcessor packetProcessor,
			PacketPool pool)
	{
		_packetFactory = packetFactory;
		_packetProcessor = packetProcessor;
		_packetPool = pool;
	}

	@Override
	public ClientConnection openChannelProtocol(TCPChannel channel) throws IOException
	{
		return new ClientConnection(channel,
				new PacketProtocol(_packetPool, _packetFactory, _packetProcessor, channel));
	}

}
