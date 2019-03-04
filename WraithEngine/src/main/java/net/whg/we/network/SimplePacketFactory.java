package net.whg.we.network;

import java.util.ArrayList;
import java.util.List;

public class SimplePacketFactory implements PacketFactory
{
	private List<PacketType> _packetTypes = new ArrayList<>();

	public void addPacketType(PacketType packetType)
	{
		if (packetType == null)
			return;

		synchronized (_packetTypes)
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

		synchronized (_packetTypes)
		{
			_packetTypes.remove(packetType);
		}
	}

	@Override
	public PacketType findPacketType(String typePath)
	{
		synchronized (_packetTypes)
		{
			for (PacketType type : _packetTypes)
				if (type.getTypePath().equals(typePath))
					return type;
			return null;
		}
	}
}
