package network_handling;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mockito;
import net.whg.we.network.DefaultPacketFactory;
import net.whg.we.network.Packet;
import net.whg.we.network.PacketPool;
import net.whg.we.network.PacketProcessor;
import net.whg.we.network.PacketType;
import net.whg.we.network.client.PacketClient;
import net.whg.we.network.server.DefaultServer;
import net.whg.we.network.server.PacketServerProtocol;
import net.whg.we.network.server.Server;

public class NetworkingTest
{
	@Rule
	public Timeout globalTimeout = Timeout.seconds(5);

	@SuppressWarnings("unchecked")
	@Test
	public void simplePacketNetwork() throws IOException, InterruptedException
	{
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
				Map<String, Object> data = (Map<String, Object>) a.getArgument(0);
				Assert.assertEquals("hello", data.get("message"));

				return null;
			}).when(handshakePacket).process(Mockito.any());
		}

		// Start Server
		Server server;
		PacketProcessor packetProcessor;
		{
			PacketPool pool = new PacketPool();
			packetProcessor = new PacketProcessor(pool);
			PacketServerProtocol serverProtocol =
					new PacketServerProtocol(packetFactory, packetProcessor, pool);

			server = new DefaultServer(serverProtocol);
			server.setPort(13579);
			server.startServer();
		}

		Thread.sleep(100);

		// Start Client
		PacketClient client = new PacketClient(packetFactory);
		client.connect("localhost", 13579);

		// Send packet from client to server.
		{
			Packet packet = client.newPacket("core.handshake");
			packet.getData().put("message", "hello");
			client.sendPacket(packet);
		}

		// Wait for packet
		{
			while (packetProcessor.getPendingPackets() == 0)
				Thread.sleep(1);

			packetProcessor.handlePackets();
		}
	}
}
