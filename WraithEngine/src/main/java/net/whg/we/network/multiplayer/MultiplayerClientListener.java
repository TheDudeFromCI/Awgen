package net.whg.we.network.multiplayer;

import net.whg.we.network.TCPChannel;
import net.whg.we.network.client.ClientListener;
import net.whg.we.network.client.DefaultClient;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketClient;
import net.whg.we.utils.logging.Log;

public class MultiplayerClientListener implements ClientListener
{
    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void onConnectToServer(DefaultClient client, TCPChannel server)
    {
        // Normally these values are retrieved from logging in. (And the token
        // should be passed from the auth server to the server directly, but,
        // eh.)
        String username = "user";
        String token = "abcdef";

        Log.infof(
                "Successfully connected to server. Sending handshake packet now. Username: %s, Token: %s",
                username, token);
        PacketClient pClient = (PacketClient) client;

        Packet packet = pClient.newPacket("auth.handshake");
        ((HandshakePacket) packet.getPacketType()).build(packet, username,
                token);
        pClient.sendPacket(packet);
    }

    @Override
    public void onDisconnectedFromServer(DefaultClient client,
            TCPChannel server)
    {
    }
}
