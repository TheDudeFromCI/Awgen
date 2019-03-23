package network_handling;

import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mockito;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.client.DefaultClient;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.PacketServer;
import net.whg.we.network.server.ServerListener;

public class ServerEventsTest
{
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void onServerConnected() throws IOException, InterruptedException
    {
        int port = 48234;
        ServerListener listener = Mockito.mock(ServerListener.class);

        PacketServer server =
                new PacketServer(new DefaultPacketFactory(), null, port);
        server.getEvents().addListener(listener);

        Thread.sleep(500);

        server.getEvents().handlePendingEvents();
        Mockito.verify(listener).onServerStarted(server);
        Mockito.verify(listener, Mockito.never()).onServerStopped(server);

        server.stopServer();
        Thread.sleep(500);

        server.getEvents().handlePendingEvents();
        Mockito.verify(listener).onServerStopped(server);
    }

    @Test
    public void onServerFailedToConnect()
            throws IOException, InterruptedException
    {
        int port = -1;
        ServerListener listener = Mockito.mock(ServerListener.class);

        PacketServer server =
                new PacketServer(new DefaultPacketFactory(), null, port);
        server.getEvents().addListener(listener);

        Thread.sleep(500);

        server.getEvents().handlePendingEvents();
        Mockito.verify(listener).onServerFailedToStart(server, port);
    }

    @Test
    public void onClientConnected() throws IOException, InterruptedException
    {
        // Setup Server
        int port = 31645;

        ServerListener listener = Mockito.mock(ServerListener.class);

        PacketServer server =
                new PacketServer(new DefaultPacketFactory(), null, port);
        server.getEvents().addListener(listener);

        Thread.sleep(500);

        // Setup Client
        ChannelProtocol protocol2 = Mockito.mock(ChannelProtocol.class);
        Mockito.doThrow(new IOException()).when(protocol2).next();

        // Client should throw an error slightly after connecting.
        new DefaultClient(protocol2, "localhost", port);
        Thread.sleep(500);

        // Tests
        server.getEvents().handlePendingEvents();
        Mockito.verify(listener).onClientConnected(Mockito.eq(server),
                Mockito.any());
        Mockito.verify(listener).onClientDisconnected(Mockito.eq(server),
                Mockito.any());
    }

    @Test
    public void stopServer_ClientDisconnectedEvent()
            throws IOException, InterruptedException
    {
        int port = 31644;

        ServerListener listener = Mockito.mock(ServerListener.class);

        PacketServer server =
                new PacketServer(new DefaultPacketFactory(), null, port);
        server.getEvents().addListener(listener);
        Thread.sleep(500);

        // Setup Client
        ChannelProtocol protocol2 = Mockito.mock(ChannelProtocol.class);
        Mockito.doThrow(new IOException()).when(protocol2).next();

        // Client should throw an error slightly after connecting.
        new DefaultClient(protocol2, "localhost", port);
        Thread.sleep(500);

        // Tests
        server.getEvents().handlePendingEvents();
        Mockito.verify(listener).onClientConnected(Mockito.eq(server),
                Mockito.any());
        Mockito.verify(listener).onClientDisconnected(Mockito.eq(server),
                Mockito.any());
    }
}
