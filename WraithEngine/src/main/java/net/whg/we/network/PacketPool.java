package net.whg.we.network;

import net.whg.we.utils.ObjectPool;

public class PacketPool extends ObjectPool<Packet>
{
	@Override
	protected Packet build()
	{
		return new Packet();
	}
}
