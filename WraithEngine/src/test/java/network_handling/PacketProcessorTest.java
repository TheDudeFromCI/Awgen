package network_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.network.packet.PacketProcessor;

public class PacketProcessorTest
{
	@Test
	public void addPackets()
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool);

		Packet packet = new Packet();
		processor.addPacket(packet);

		Assert.assertEquals(1, processor.getPendingPackets());
	}

	@Test
	public void onPacketRecieved()
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool);

		Packet packet = new Packet();
		processor.onPacketRecieved(packet);

		Assert.assertEquals(1, processor.getPendingPackets());
	}

	@Test
	public void handlePackets()
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool);

		Packet packet = Mockito.mock(Packet.class);
		processor.addPacket(packet);

		processor.handlePackets();
		Mockito.verify(packet).process();

		Assert.assertEquals(packet, pool.get());
		Assert.assertEquals(0, processor.getPendingPackets());
	}

	@Test
	public void addPacket_Twice()
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool);

		Packet packet = new Packet();
		processor.addPacket(packet);
		processor.addPacket(packet);

		Assert.assertEquals(1, processor.getPendingPackets());
	}

	@Test
	public void addPacket_Null()
	{
		PacketPool pool = new PacketPool();
		PacketProcessor processor = new PacketProcessor(pool);

		processor.addPacket(null);

		Assert.assertEquals(0, processor.getPendingPackets());
	}

	@Test
	public void onPacketSent()
	{
		/*
		 * This method is empty in the class and is only there for the listener to work.
		 * But for some reason, the coverage thing marks it as uncalled code, despite
		 * being empty. So I'm just leaving this here for an extra line of test
		 * coverage. Shrug.
		 */

		new PacketProcessor(new PacketPool()).onPacketSent(null);
	}
}
