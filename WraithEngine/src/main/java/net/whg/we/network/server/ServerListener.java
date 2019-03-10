package net.whg.we.network.server;

import net.whg.we.event.Listener;

public interface ServerListener extends Listener
{
    void onServerStarted(Server server);

    void onServerFailedToStart(Server server, int port);

    void onClientConnected(Server server, ClientConnection client);

    void onClientDisconnected(Server server, ClientConnection client);

    void onServerStopped(Server server);
}
