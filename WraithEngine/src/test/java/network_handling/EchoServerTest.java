package network_handling;

import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.ClientEvent;
import net.whg.we.network.multiplayer.ServerEvent;
import net.whg.we.network.netty.Client;
import net.whg.we.network.netty.Server;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;

public class EchoServerTest
{

	@Test(timeout = 5000)
	public void echoServer() throws InterruptedException
	{
		final int port = 8123;

		// Build packet handles
		StoreMessagePacketHandler serverHandler = new StoreMessagePacketHandler();
		StoreMessagePacketHandler clientHandler = new StoreMessagePacketHandler();
		PacketManagerHandler packetManagerServer =
				PacketManagerHandler.createPacketManagerHandler(serverHandler, false);
		PacketManagerHandler packetManagerClient =
				PacketManagerHandler.createPacketManagerHandler(clientHandler, false);

		// Build dummy echo packet
		PacketType echoPacket = Mockito.mock(PacketType.class);
		Mockito.when(echoPacket.getTypePath()).thenReturn("test.echo");
		((DefaultPacketFactory) packetManagerServer.factory()).addPacketType(echoPacket);
		((DefaultPacketFactory) packetManagerClient.factory()).addPacketType(echoPacket);

		Mockito.doAnswer(a ->
		{
			Packet packet = a.getArgument(0);
			ByteWriter out = packet.getByteWriter();

			String message = (String) packet.getData().get("message");
			boolean client = (boolean) packet.getData().get("client");

			out.writeString(message, StandardCharsets.UTF_8);
			out.writeByte(client ? 1 : 0);

			return out.getPos();
		}).when(echoPacket).encode(Mockito.any());

		Mockito.doAnswer(a ->
		{
			Packet packet = a.getArgument(0);
			ByteReader in = packet.getByteReader();

			String message = in.getString(StandardCharsets.UTF_8);
			boolean client = in.getByte() != 0;

			packet.getData().put("message", message);
			packet.getData().put("client", client);

			return null;
		}).when(echoPacket).decode(Mockito.any());

		Mockito.doAnswer(a ->
		{
			Packet packet = a.getArgument(0);
			StoreMessagePacketHandler handler = a.getArgument(1);

			handler.message = (String) packet.getData().get("message");

			if ((boolean) packet.getData().get("client"))
			{
				// Send packet from server to client
				Packet p2 = packetManagerServer.pool().get();
				p2.setPacketType(packetManagerServer.factory().findPacketType("test.echo"));
				p2.getData().put("message", "Hello Client!");
				p2.getData().put("client", false);

				UserConnection con = packet.getSender();
				con.sendPacket(p2);
			}

			return null;
		}).when(echoPacket).process(Mockito.any(), Mockito.any());

		// Build and start server and client
		ServerEvent serverEvent = Mockito.mock(ServerEvent.class);
		ClientEvent clientEvent = Mockito.mock(ClientEvent.class);
		Server server = new Server(port, packetManagerServer, serverEvent);
		Client client = new Client("localhost", port, packetManagerClient, clientEvent);
		server.start();
		client.start();

		// Ensure default state
		Assert.assertNull(serverHandler.message);
		Assert.assertNull(clientHandler.message);

		// Send packet from client to server
		Packet packet = packetManagerClient.pool().get();
		packet.setPacketType(packetManagerClient.factory().findPacketType("test.echo"));
		packet.getData().put("message", "Hello Server!");
		packet.getData().put("client", true);
		client.send(packet);

		// Let server process packets
		Thread.sleep(1000);
		packetManagerServer.processor().handlePackets();

		// Let clientr process packets
		Thread.sleep(1000);
		packetManagerClient.processor().handlePackets();

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
