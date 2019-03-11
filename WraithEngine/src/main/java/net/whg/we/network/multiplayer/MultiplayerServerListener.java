package net.whg.we.network.multiplayer;

import net.whg.we.network.server.ClientConnection;
import net.whg.we.network.server.Server;
import net.whg.we.network.server.ServerListener;
import net.whg.we.utils.logging.Log;

public class MultiplayerServerListener implements ServerListener
{
    private MultiplayerServer _server;

    public MultiplayerServerListener(MultiplayerServer server)
    {
        _server = server;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public void onServerStarted(Server server)
    {
        Log.info("Server successfully started.");
    }

    @Override
    public void onServerFailedToStart(Server server, int port)
    {
        Log.warnf("Failed to start server on port %d!", port);
    }

    @Override
    public void onClientConnected(Server server, ClientConnection client)
    {
        Log.debugf("Adding client to pending connection list. IP: %s",
                client.getIP());
        _server.getPendingClients().addClient(client);
    }

    @Override
    public void onClientDisconnected(Server server, ClientConnection client)
    {
        _server.getPendingClients().removeClient(client);
        _server.getPlayerList().removePlayer(client);
    }

    @Override
    public void onServerStopped(Server server)
    {
        _server.getPendingClients().clear();
        _server.getPlayerList().clear();
    }
}
