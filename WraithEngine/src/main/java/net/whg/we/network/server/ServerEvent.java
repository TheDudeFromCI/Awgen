package net.whg.we.network.server;

import net.whg.we.event.EventCallerBase;

public class ServerEvent extends EventCallerBase<ServerListener>
{
    private static final int SERVER_STARTED_EVENT = 0;
    private static final int SERVER_FAILED_TO_START_EVENT = 1;
    private static final int CLIENT_CONNECTED_EVENT = 2;
    private static final int CLIENT_DISCONNECTED_EVENT = 3;
    private static final int SERVER_STOPPED_EVENT = 4;

    private Server _server;

    public ServerEvent(Server server)
    {
        _server = server;
    }

    public void onServerStarted()
    {
        callEvent(SERVER_STARTED_EVENT);
    }

    public void onServerFailedToStart(int port)
    {
        callEvent(SERVER_FAILED_TO_START_EVENT, port);
    }

    public void onClientConnected(ClientConnection client)
    {
        callEvent(CLIENT_CONNECTED_EVENT, client);
    }

    public void onClientDisconnected(ClientConnection client)
    {
        callEvent(CLIENT_DISCONNECTED_EVENT, client);
    }

    public void onServerStopped()
    {
        callEvent(SERVER_STOPPED_EVENT);
    }

    @Override
    protected void runEvent(ServerListener listener, int e, Object arg)
    {
        switch (e)
        {
            case SERVER_STARTED_EVENT:
                listener.onServerStarted(_server);
                return;

            case SERVER_FAILED_TO_START_EVENT:
                listener.onServerFailedToStart(_server, (int) arg);
                return;

            case CLIENT_CONNECTED_EVENT:
                listener.onClientConnected(_server, (ClientConnection) arg);
                return;

            case CLIENT_DISCONNECTED_EVENT:
                listener.onClientDisconnected(_server, (ClientConnection) arg);
                return;

            case SERVER_STOPPED_EVENT:
                listener.onServerStopped(_server);
                return;

            default:
                throw new IllegalArgumentException("Unknown event id! " + e);
        }
    }
}