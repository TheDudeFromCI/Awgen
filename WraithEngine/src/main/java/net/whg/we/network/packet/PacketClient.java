package net.whg.we.network.packet;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import net.whg.we.network.DefaultTCPChannel;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.client.ClientThread;
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

	public PacketClient(PacketFactory packetFactory)
	{
		super(buildPacketProtocol(packetFactory));

		_protocol = (PacketProtocol) getProtocol();
		_pool = _protocol.getPacketPool();
		_processor = (PacketProcessor) _protocol.getListener();
		_factory = _protocol.getPacketFactory();
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
			Log.errorf("There has been an error while attempting to send a packet!", e);

			try
			{
				close();
			}
			catch (IOException e1)
			{
				Log.errorf("There has been an error while attempting to close client" + "socket!",
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

	@Override
	public TCPChannel connect(String ip, int port) throws UnknownHostException, IOException
	{
		_channel = new DefaultTCPChannel(new Socket(ip, port), true);
		_protocol.init(_channel.getInputStream(), _channel.getOutputStream());
		_protocol.setSender(_channel);
		_clientThread = new ClientThread(_channel, _protocol);
		_clientThread.start();

		return _channel;
	}
}
