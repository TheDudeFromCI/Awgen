package net.whg.we.network.multiplayer;

import net.whg.we.network.multiplayer.HandshakePacket;
import net.whg.we.network.packet.DefaultPacketFactory;

public class MultiplayerUtils
{
    public static void addDefaultPackets(DefaultPacketFactory factory)
    {
        factory.addPacketType(new HandshakePacket());
    }
}
