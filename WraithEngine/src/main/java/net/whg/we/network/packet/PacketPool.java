package net.whg.we.network.packet;

import net.whg.we.utils.ObjectPool;

public class PacketPool extends ObjectPool<Packet>
{
	@Override
	protected Packet build()
	{
		return new Packet();
	}
}
