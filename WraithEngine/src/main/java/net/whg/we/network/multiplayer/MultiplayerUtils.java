package net.whg.we.network.multiplayer;

import net.whg.we.network.multiplayer.HandshakePacket;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.packets.*;

public class MultiplayerUtils
{
    public static void addDefaultPackets(DefaultPacketFactory factory)
    {
        factory.addPacketType(new HandshakePacket());
        factory.addPacketType(new TerminalCommandPacket());
        factory.addPacketType(new TerminalOutputPacket());
        factory.addPacketType(new ChatPacket());
        factory.addPacketType(new PlayerJoinPacket());
    }
}
