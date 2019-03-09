package net.whg.we.network.client;

import net.whg.we.event.Listener;
import net.whg.we.network.TCPChannel;

public interface ClientListener extends Listener
{
    void onConnectToServer(DefaultClient client, TCPChannel server);

    void onDisconnectedFromServer(DefaultClient client, TCPChannel server);
}
