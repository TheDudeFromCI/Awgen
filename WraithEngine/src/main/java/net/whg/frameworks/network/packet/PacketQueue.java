package net.whg.frameworks.network.packet;

import java.util.LinkedList;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.util.GenericRunnable;

public class PacketQueue
{
	private PacketPool _pool;
	private LinkedList<Packet> _queue = new LinkedList<>();

	public PacketQueue(PacketPool pool)
	{
		_pool = pool;
	}

	public void addPacket(Packet packet)
	{
		if (packet == null)
			return;

		synchronized (_queue)
		{
			_queue.add(packet);
		}
	}

	public void handlePackets(GenericRunnable<Packet> action)
	{
		Packet packet;
		while (true)
		{
			synchronized (_queue)
			{
				if (_queue.isEmpty())
					return;
				packet = _queue.removeFirst();
			}

			try
			{
				action.run(packet);
			}
			catch (Exception e)
			{
				Log.errorf("Failed to handle queued packet!", e);
			}

			_pool.put(packet);
		}
	}
}
