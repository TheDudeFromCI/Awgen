package net.whg.frameworks.network.packet;

import java.util.ArrayList;
import java.util.List;

public class DefaultPacketFactory implements PacketFactory
{
	private Object LOCK = new Object();
	private List<PacketType> _packetTypes = new ArrayList<>();

	public void addPacketType(PacketType packetType)
	{
		if (packetType == null)
			return;

		synchronized (LOCK)
		{
			if (_packetTypes.contains(packetType))
				return;

			_packetTypes.add(packetType);
		}
	}

	public void removePacketType(PacketType packetType)
	{
		if (packetType == null)
			return;

		synchronized (LOCK)
		{
			_packetTypes.remove(packetType);
		}
	}

	@Override
	public PacketType findPacketType(String typePath)
	{
		if (typePath == null)
			return null;

		synchronized (LOCK)
		{
			for (PacketType type : _packetTypes)
				if (type.getTypePath().equals(typePath))
					return type;
			return null;
		}
	}
}
