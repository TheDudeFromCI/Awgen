package net.whg.frameworks.network.packet;

import net.whg.frameworks.util.ObjectPool;

public class PacketPool extends ObjectPool<Packet>
{
	@Override
	protected Packet build()
	{
		return new Packet();
	}
}
