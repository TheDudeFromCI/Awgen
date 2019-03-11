package net.whg.we.network.multiplayer;

import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketProtocol;
import net.whg.we.network.server.ClientConnection;
import net.whg.we.utils.logging.Log;

public class OnlinePlayer implements Player
{
    private String _username;
    private String _token;
    private ClientConnection _client;
    private PacketProtocol _packetProtocol;

    public OnlinePlayer(ClientConnection client, String username, String token)
    {
        _client = client;
        _username = username;
        _token = token;
        _packetProtocol = (PacketProtocol) _client.getProtocol();
    }

    @Override
    public String getUsername()
    {
        return _username;
    }

    @Override
    public String getUserToken()
    {
        return _token;
    }

    /**
     * Sends a packet to this user.
     * 
     * @param packet
     *                   - The packet to send.
     */
    public void sendPacket(Packet packet)
    {
        try
        {
            _packetProtocol.sendPacket(packet);
        }
        catch (Exception exception)
        {
            Log.errorf(
                    "There has been an error while attempt to send a packet to a player!",
                    exception);
        }
    }

    /**
     * Kicks a player from the server.
     */
    public void kick()
    {
        _client.close();
    }

    ClientConnection getClientConnection()
    {
        return _client;
    }
}
