package network_handling;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.main.GameState;
import net.whg.we.network.netty.Client;
import net.whg.we.network.netty.Server;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.network.packet.PacketProcessor;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class EchoServerTest
{
	@Test
	public void echoServer() throws InterruptedException
	{
		Log.setLogLevel(Log.TRACE);
		final int port = 8123;

		// Build packet handles
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();

		StoreMessagePacketHandler serverHandler = new StoreMessagePacketHandler();
		StoreMessagePacketHandler clientHandler = new StoreMessagePacketHandler();

		PacketProcessor listenerServer = new PacketProcessor(pool, serverHandler);
		PacketProcessor listenerClient = new PacketProcessor(pool, clientHandler);

		// Build dummy echo packet
		PacketType echoPacket = Mockito.mock(PacketType.class);
		Mockito.when(echoPacket.getTypePath()).thenReturn("test.echo");
		factory.addPacketType(echoPacket);

		Mockito.doAnswer(a ->
		{
			ByteWriter out = new ByteWriter(a.getArgument(0));
			Map<String, Object> data = a.getArgument(1);
			String message = (String) data.get("message");
			out.writeString(message, StandardCharsets.UTF_8);
			return out.getPos();
		}).when(echoPacket).encode(Mockito.any(), Mockito.any());
		Mockito.doAnswer(a ->
		{
			ByteReader in = new ByteReader(a.getArgument(0));
			String message = in.getString(StandardCharsets.UTF_8);
			Map<String, Object> data = a.getArgument(2);
			data.put("message", message);
			return null;
		}).when(echoPacket).decode(Mockito.any(), Mockito.anyInt(), Mockito.any());
		Mockito.doAnswer(a ->
		{
			Packet packet = a.getArgument(0);
			StoreMessagePacketHandler handler = a.getArgument(1);
			handler.message = (String) packet.getData().get("message");
			return null;
		}).when(echoPacket).process(Mockito.any(), Mockito.any());

		// Build and start server and client
		Server server = new Server(port, pool, factory, listenerServer);
		Client client = new Client("localhost", port, pool, factory, listenerClient);
		server.start();
		client.start();

		// Ensure default state
		Assert.assertNull(serverHandler.message);
		Assert.assertNull(clientHandler.message);

		// Send packet from client to server
		Packet packet = pool.get();
		packet.setPacketType(factory.findPacketType("test.echo"));
		packet.getData().put("message", "Hello Server!");
		client.send(packet);

		// Let server process packets
		Thread.sleep(1000);
		listenerServer.handlePackets();

		// Let clientr process packets
		Thread.sleep(1000);
		listenerClient.handlePackets();

		// Test if information passed through
		Assert.assertEquals("Hello Server!", serverHandler.message);
		Assert.assertEquals("Hello Client!", clientHandler.message);

		// Shutdown servers
		server.stop();
		client.stop();
	}

	public class StoreMessagePacketHandler implements PacketHandler
	{
		public String message;

		@Override
		public void setGameState(GameState gameState)
		{
		}

		@Override
		public boolean isClient()
		{
			return false;
		}

		@Override
		public GameState getGameState()
		{
			return null;
		}
	}
}
