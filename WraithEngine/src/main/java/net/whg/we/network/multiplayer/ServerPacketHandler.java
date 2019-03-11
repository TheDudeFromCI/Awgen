package net.whg.we.network.multiplayer;

import net.whg.we.network.packet.PacketHandler;

public class ServerPacketHandler implements PacketHandler
{
    private MultiplayerServer _server;

    public ServerPacketHandler(MultiplayerServer server)
    {
        _server = server;
    }

    public MultiplayerServer getServer()
    {
        return _server;
    }
}
