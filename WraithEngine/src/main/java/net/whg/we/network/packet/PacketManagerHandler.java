package net.whg.we.network.packet;

import net.whg.we.network.multiplayer.MultiplayerUtils;

public class PacketManagerHandler extends PacketManager
{
	public static PacketManagerHandler createPacketManagerHandler(PacketHandler handler,
			boolean defaultPackets)
	{
		PacketPool packetPool = new PacketPool();
		DefaultPacketFactory packetFactory = new DefaultPacketFactory();
		PacketProcessor packetProcessor = new PacketProcessor(packetPool, handler);
		PacketManagerHandler packetManager =
				new PacketManagerHandler(packetPool, packetFactory, packetProcessor);

		if (defaultPackets)
			MultiplayerUtils.addDefaultPackets(packetFactory);

		return packetManager;
	}

	private PacketProcessor _packetProcessor;

	public PacketManagerHandler(PacketPool packetPool, PacketFactory packetFactory,
			PacketProcessor packetProcessor)
	{
		super(packetPool, packetFactory);
		_packetProcessor = packetProcessor;
	}

	public PacketProcessor processor()
	{
		return _packetProcessor;
	}
}
