package network_handling;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mockito;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketClient;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketServer;
import net.whg.we.network.packet.PacketType;
import net.whg.we.network.server.ServerListener;

public class NetworkingTest
{
	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	@SuppressWarnings("unchecked")
	@Test
	public void simplePacketNetwork() throws IOException, InterruptedException
	{
		// This test sends a packet from the client to the server which is
		// then loaded and processed.

		int port = 13579;

		// Packet Encoding rules
		DefaultPacketFactory packetFactory = new DefaultPacketFactory();

		// Handshake packet
		{
			PacketType handshakePacket = Mockito.mock(PacketType.class);
			Mockito.when(handshakePacket.getTypePath()).thenReturn("core.handshake");
			packetFactory.addPacketType(handshakePacket);

			Mockito.doAnswer(a ->
			{
				byte[] bytes = a.getArgument(0);
				String message = (String) ((Map<String, Object>) a.getArgument(1)).get("message");

				byte[] messageAsBytes = message.getBytes(StandardCharsets.UTF_8);
				for (int i = 0; i < messageAsBytes.length; i++)
					bytes[i] = messageAsBytes[i];

				return messageAsBytes.length;
			}).when(handshakePacket).encode(Mockito.any(), Mockito.any());

			Mockito.doAnswer(a ->
			{
				byte[] bytes = a.getArgument(0);
				int length = a.getArgument(1);
				Map<String, Object> data = (Map<String, Object>) a.getArgument(2);

				byte[] messageAsBytes = new byte[length];
				for (int i = 0; i < messageAsBytes.length; i++)
					messageAsBytes[i] = bytes[i];

				data.put("message", new String(messageAsBytes, StandardCharsets.UTF_8));

				return null;
			}).when(handshakePacket).decode(Mockito.any(), Mockito.anyInt(), Mockito.any());

			Mockito.doAnswer(a ->
			{
				Packet packet = (Packet) a.getArgument(0);
				Assert.assertEquals("hello", packet.getData().get("message"));

				return null;
			}).when(handshakePacket).process(Mockito.any(), Mockito.any());
		}

		// Start Server
		PacketServer server = new PacketServer(packetFactory, null, port);
		Thread.sleep(100);

		// Start Client
		PacketClient client = new PacketClient(packetFactory, null, "localhost", port);

		// Send packet from client to server.
		{
			Packet packet = client.newPacket("core.handshake");
			packet.getData().put("message", "hello");
			client.sendPacket(packet);
		}

		// Wait for packet
		{
			while (true)
			{
				server.handlePackets();
				Thread.sleep(1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void simplePacketNetwork_SendToClient() throws IOException, InterruptedException
	{
		// This test sends a packet from the server to the client.

		int port = 53655;

		// Packet Encoding rules
		DefaultPacketFactory packetFactory = new DefaultPacketFactory();

		// Handshake packet
		{
			PacketType handshakePacket = Mockito.mock(PacketType.class);
			Mockito.when(handshakePacket.getTypePath()).thenReturn("core.handshake");
			packetFactory.addPacketType(handshakePacket);

			Mockito.doAnswer(a ->
			{
				byte[] bytes = a.getArgument(0);
				String message = (String) ((Map<String, Object>) a.getArgument(1)).get("message");

				byte[] messageAsBytes = message.getBytes(StandardCharsets.UTF_8);
				for (int i = 0; i < messageAsBytes.length; i++)
					bytes[i] = messageAsBytes[i];

				return messageAsBytes.length;
			}).when(handshakePacket).encode(Mockito.any(), Mockito.any());

			Mockito.doAnswer(a ->
			{
				byte[] bytes = a.getArgument(0);
				int length = a.getArgument(1);
				Map<String, Object> data = (Map<String, Object>) a.getArgument(2);

				byte[] messageAsBytes = new byte[length];
				for (int i = 0; i < messageAsBytes.length; i++)
					messageAsBytes[i] = bytes[i];

				data.put("message", new String(messageAsBytes, StandardCharsets.UTF_8));

				return null;
			}).when(handshakePacket).decode(Mockito.any(), Mockito.anyInt(), Mockito.any());

			Mockito.doAnswer(a ->
			{
				Packet packet = (Packet) a.getArgument(0);
				Assert.assertEquals("hello", packet.getData().get("message"));

				return null;
			}).when(handshakePacket).process(Mockito.any(), Mockito.any());
		}

		PacketServer server = new PacketServer(packetFactory, null, port);
		Thread.sleep(100);

		// Start Client
		PacketClient client = new PacketClient(packetFactory, null, "localhost", port);

		Thread.sleep(500);

		// Send packet from server to client.
		{
			Packet packet = server.newPacket("core.handshake");
			packet.getData().put("message", "hello");
			server.sendPacket(packet, server.getClientList().getClient(0));
		}

		// Wait for packet
		{
			while (client.getPacketProcessor().getPendingPackets() == 0)
				Thread.sleep(1);

			client.handlePackets();
		}
	}

	@Test
	public void kickClientFromServer() throws IOException, InterruptedException
	{
		int port = 34534;
		ServerListener listener = Mockito.mock(ServerListener.class);

		PacketFactory factory = new DefaultPacketFactory();

		PacketServer server = new PacketServer(factory, null, port);
		server.getEvents().addListener(listener);
		Thread.sleep(100);

		new PacketClient(factory, null, "localhost", port);
		Thread.sleep(500);

		server.getEvents().handlePendingEvents();
		Mockito.verify(listener, Mockito.never()).onClientDisconnected(Mockito.eq(server),
				Mockito.any());

		server.getClientList().getClient(0).close();
		Thread.sleep(500);

		server.getEvents().handlePendingEvents();
		Mockito.verify(listener).onClientDisconnected(Mockito.eq(server), Mockito.any());
	}
}
