package net.whg.we.network;

import java.util.LinkedList;
import java.util.List;

public class PacketProcessor implements PacketListener
{
	private Object LOCK = new Object();
	private List<Packet> _packets = new LinkedList<>();
	private PacketPool _packetPool;

	public PacketProcessor(PacketPool packetPool)
	{
		_packetPool = packetPool;
	}

	public void addPacket(Packet packet)
	{
		if (packet == null)
			return;

		synchronized (LOCK)
		{
			if (_packets.contains(packet))
				return;

			_packets.add(packet);
		}
	}

	public void handlePackets()
	{
		synchronized (LOCK)
		{
			for (Packet packet : _packets)
			{
				packet.process();
				_packetPool.put(packet);
			}
			_packets.clear();
		}
	}

	@Override
	public void onPacketSent(Packet packet)
	{
	}

	@Override
	public void onPacketRecieved(Packet packet)
	{
		addPacket(packet);
	}

	public int getPendingPackets()
	{
		synchronized (LOCK)
		{
			return _packets.size();
		}
	}
}
