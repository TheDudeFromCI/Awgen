package net.whg.we.network.client;

import net.whg.we.event.EventCallerBase;
import net.whg.we.network.TCPChannel;

public class ClientEvent extends EventCallerBase<ClientListener>
{
    private static final int CONNECT_TO_SERVER_EVENT = 0;
    private static final int DISCONNECTED_FROM_SERVER_EVENT = 1;

    private DefaultClient _client;

    public ClientEvent(DefaultClient client)
    {
        _client = client;
    }

    public void onConnectToServer(TCPChannel server)
    {
        callEvent(CONNECT_TO_SERVER_EVENT, server);
    }

    public void onDisconnectedFromServer(TCPChannel server)
    {
        callEvent(DISCONNECTED_FROM_SERVER_EVENT, server);
    }

    @Override
    protected void runEvent(ClientListener listener, int index, Object arg)
    {
        switch (index)
        {
            case CONNECT_TO_SERVER_EVENT:
                listener.onConnectToServer(_client, (TCPChannel) arg);
                return;

            case DISCONNECTED_FROM_SERVER_EVENT:
                listener.onDisconnectedFromServer(_client, (TCPChannel) arg);
                return;

            default:
                throw new IllegalArgumentException(
                        "Unknown event index! " + index);
        }
    }
}
