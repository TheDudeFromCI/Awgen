package net.whg.we.network.multiplayer;

import java.io.IOException;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketServer;
import net.whg.we.server_logic.connect.ServerPlayerList;
import net.whg.we.utils.logging.Log;

public class MultiplayerServer
{
    public static final int DEFAULT_PORT = 23423;

    private PacketServer _server;
    private DefaultPacketFactory _factory;
    private ServerPlayerList _playerList;
    private PendingClients _pendingClients;
    private ServerPacketHandler _packetHandler;

    public MultiplayerServer()
    {
        _playerList = new ServerPlayerList();
        _pendingClients = new PendingClients();
        _packetHandler = new ServerPacketHandler(this);

        _factory = new DefaultPacketFactory();
        MultiplayerUtils.addDefaultPackets(_factory);
    }

    public boolean isRunning()
    {
        return _server != null && _server.isRunning();
    }

    public PacketServer getServer()
    {
        return _server;
    }

    public PacketFactory getPacketFactory()
    {
        return _factory;
    }

    public void startServer()
    {
        startServer(DEFAULT_PORT);
    }

    public void startServer(int port)
    {
        if (isRunning())
            throw new IllegalStateException("Server is already running!");

        try
        {
            Log.infof("Opening multiplayer server on port %d.", port);
            _server = new PacketServer(_factory, _packetHandler, port);
            _server.getEvents()
                    .addListener(new MultiplayerServerListener(this));
        }
        catch (IOException e)
        {
            Log.errorf(
                    "There has been an error while trying to start this server!",
                    e);
            _server = null;
        }
    }

    public void stopServer()
    {
        Log.info("Closing multiplayer server.");
        _server.stopServer();
        _server = null;
    }

    public ServerPlayerList getPlayerList()
    {
        return _playerList;
    }

    public void updatePhysics()
    {
        if (!isRunning())
            return;

        _server.getEvents().handlePendingEvents();
        _pendingClients.update();
        _server.handlePackets();
    }

    PendingClients getPendingClients()
    {
        return _pendingClients;
    }

    public ServerPacketHandler getPacketHandler()
    {
        return _packetHandler;
    }
}
