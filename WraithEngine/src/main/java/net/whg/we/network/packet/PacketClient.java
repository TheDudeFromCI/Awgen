package net.whg.we.network.packet;

import java.io.IOException;
import net.whg.we.network.client.DefaultClient;
import net.whg.we.utils.logging.Log;

public class PacketClient extends DefaultClient
{
	private static PacketProtocol buildPacketProtocol(PacketFactory factory)
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool);
		return new PacketProtocol(pool, factory, processor);
	}

	private PacketPool _pool;
	private PacketProtocol _protocol;
	private PacketProcessor _processor;
	private PacketFactory _factory;

	public PacketClient(PacketFactory packetFactory, String ip, int port)
			throws IOException
	{
		super(buildPacketProtocol(packetFactory), ip, port);

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
		try
		{
			_protocol.sendPacket(packet);
		}
		catch (IOException e)
		{
			Log.errorf(
					"There has been an error while attempting to send a packet!",
					e);

			try
			{
				close();
			}
			catch (IOException e1)
			{
				Log.errorf(
						"There has been an error while attempting to close client"
								+ "socket!",
						e1);
			}
		}

		_pool.put(packet);
	}

	public Packet newPacket(String typePath)
	{
		Packet packet = _pool.get();
		packet.setPacketType(_factory.findPacketType(typePath));

		return packet;
	}
}
