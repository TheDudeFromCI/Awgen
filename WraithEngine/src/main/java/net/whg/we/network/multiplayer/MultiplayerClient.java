package net.whg.we.network.multiplayer;

import java.io.IOException;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketClient;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.utils.logging.Log;

public class MultiplayerClient
{
    private DefaultPacketFactory _factory;
    private PacketClient _client;
    private ClientPacketHandler _handler;
    private String _username;
    private String _token;

    public MultiplayerClient(String username, String token)
    {
        _username = username;
        _token = token;

        _handler = new ClientPacketHandler();

        _factory = new DefaultPacketFactory();
        MultiplayerUtils.addDefaultPackets(_factory);
    }

    public boolean isRunning()
    {
        return _client != null;
    }

    public PacketClient getClient()
    {
        return _client;
    }

    public void startClient(String ip)
    {
        startClient(ip, MultiplayerServer.DEFAULT_PORT);
    }

    public void startClient(String ip, int port)
    {
        if (isRunning())
            throw new IllegalStateException("Server is already running!");

        try
        {
            Log.infof("Opening multiplayer client on ip %s, port %d.", ip,
                    port);
            _client = new PacketClient(_factory, _handler, ip, port);
            _client.getEvents()
                    .addListener(new MultiplayerClientListener(this));
        }
        catch (IOException e)
        {
            Log.errorf(
                    "There has been an error while trying to start this client!",
                    e);
        }
    }

    public PacketFactory getPacketFactory()
    {
        return _factory;
    }

    public void stopClient()
    {
        if (!isRunning())
            return;

        Log.info("Closing multiplayer client.");

        try
        {
            _client.close();
        }
        catch (IOException e)
        {
            Log.errorf(
                    "There has been an error while trying to close the client!",
                    e);
        }

        _client = null;
    }

    public void updatePhysics()
    {
        if (!isRunning())
            return;

        _client.getEvents().handlePendingEvents();
    }

    public Packet newPacket(String type)
    {
        if (!isRunning())
            throw new IllegalStateException("Client socket not open!");

        return _client.newPacket(type);
    }

    public void sendPacket(Packet packet)
    {
        if (!isRunning())
            throw new IllegalStateException("Client socket not open!");

        _client.sendPacket(packet);
    }

    public String getUsername()
    {
        return _username;
    }

    public String getToken()
    {
        return _token;
    }

    public ClientPacketHandler getPacketHandler()
    {
        return _handler;
    }
}
