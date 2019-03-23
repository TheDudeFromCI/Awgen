package network_handling.multiplayer;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.network.multiplayer.MultiplayerServer;

public class MultiplayerServerTest
{
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    @Test
    public void playerConnect() throws InterruptedException
    {
        int port = 34579;

        // Start server
        MultiplayerServer server = new MultiplayerServer();
        server.startServer(port);

        // Make sure no one is online
        Thread.sleep(500);
        server.updatePhysics();
        Assert.assertEquals(0, server.getPlayerList().getPlayerCount());

        // Start client
        MultiplayerClient client = new MultiplayerClient("username", "token");
        client.startClient("localhost", port);

        // Let client send packet
        Thread.sleep(500);
        client.updatePhysics();

        // Make sure client has connected
        Thread.sleep(500);
        server.updatePhysics();
        Assert.assertEquals(1, server.getPlayerList().getPlayerCount());
    }
}
