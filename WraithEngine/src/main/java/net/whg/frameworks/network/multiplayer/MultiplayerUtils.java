package net.whg.frameworks.network.multiplayer;

import net.whg.frameworks.network.packet.DefaultPacketFactory;
import net.whg.we.packets.ChatPacket;
import net.whg.we.packets.ImportRequestPacket;
import net.whg.we.packets.PlayerJoinPacket;
import net.whg.we.packets.PlayerLeavePacket;
import net.whg.we.packets.PlayerMovePacket;
import net.whg.we.packets.TerminalCommandPacket;
import net.whg.we.packets.TerminalOutputPacket;

public class MultiplayerUtils
{
	public static void addDefaultPackets(DefaultPacketFactory factory)
	{
		factory.addPacketType(new HandshakePacket());
		factory.addPacketType(new TerminalCommandPacket());
		factory.addPacketType(new TerminalOutputPacket());
		factory.addPacketType(new ChatPacket());
		factory.addPacketType(new PlayerJoinPacket());
		factory.addPacketType(new PlayerMovePacket());
		factory.addPacketType(new PlayerLeavePacket());
		factory.addPacketType(new ImportRequestPacket());
	}
}
