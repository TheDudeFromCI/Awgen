package net.whg.we.network.packet;

import java.io.IOException;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.server.ClientConnection;
import net.whg.we.network.server.ServerProtocol;

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
