package network_handling;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.packet.DefaultPacketFactory;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketListener;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.network.packet.PacketProtocol;
import net.whg.we.network.packet.PacketType;

public class PacketProtocolTest
{
	@Test
	public void init() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		protocol.init(in, out);
		protocol.close();

		Mockito.verify(in).close();
		Mockito.verify(out).close();
	}

	@Test
	public void sendPacket() throws IOException
	{
		PacketPool pool = new PacketPool();
		PacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		PipedInputStream outputReader = new PipedInputStream();
		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = new BufferedOutputStream(new PipedOutputStream(outputReader));

		protocol.init(in, out);

		PacketType type = Mockito.mock(PacketType.class);
		Mockito.when(type.encode(Mockito.any(), Mockito.any())).then(a ->
		{
			byte[] bytes = a.getArgument(0);
			bytes[0] = (byte) 10;
			bytes[1] = (byte) 11;
			bytes[2] = (byte) 12;
			return 3;
		});
		Mockito.when(type.getTypePath()).thenReturn("a");

		Packet packet = new Packet();
		packet.setPacketType(type);

		Assert.assertEquals(0, packet.getBytes()[0]);
		Assert.assertEquals(0, packet.getBytes()[1]);
		Assert.assertEquals(0, packet.getBytes()[2]);

		protocol.sendPacket(packet);
		protocol.close();

		byte[] output = new byte[7];
		outputReader.read(output);

		byte[] expected = new byte[]
		{
				// Header
				0x00, 0x03,

				// Name path
				0x01, 0x61,

				// Packet
				0x0A, 0x0B, 0x0C,
		};

		Assert.assertArrayEquals(expected, output);
		Assert.assertEquals(0, outputReader.available());
	}

	@Test
	public void sendPacket_Null() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		protocol.init(in, out);
		protocol.sendPacket(null);
		protocol.close();

		Mockito.verify(out, Mockito.never()).write(Mockito.any(), Mockito.anyInt(),
				Mockito.anyInt());
	}

	@Test(expected = IllegalStateException.class)
	public void sendPacket_AlreadyClosed() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		PacketType type = Mockito.mock(PacketType.class);

		protocol.init(in, out);
		protocol.close();

		Packet packet = new Packet();
		packet.setPacketType(type);
		protocol.sendPacket(packet);
	}

	@Test
	public void sendPacket_NoPacketType() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		protocol.init(in, out);
		protocol.sendPacket(new Packet());
		protocol.close();

		Mockito.verify(out, Mockito.never()).write(Mockito.any(), Mockito.anyInt(),
				Mockito.anyInt());
	}

	@Test(expected = IllegalArgumentException.class)
	public void sendPacket_VeryLongPacketName() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		PacketType type = Mockito.mock(PacketType.class);
		Mockito.when(type.getTypePath())
				.thenReturn("A very, very, very, very long package name."
						+ "With even more words and lines and characters that's simply far too long"
						+ "to all fit into the tiny 255 character byte buffer limit. I made sure"
						+ "to add a ton of text here because this is a bad packet name and I have"
						+ "no idea why anyone would need a packet name this long.");

		Packet packet = new Packet();
		packet.setPacketType(type);

		protocol.init(in, out);
		protocol.sendPacket(packet);
		protocol.close();
	}

	@Test
	public void closeProtocolTwice() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		protocol.init(in, out);
		protocol.close();
		protocol.close();
		protocol.onDisconnected();

		Mockito.verify(in, Mockito.times(1)).close();
		Mockito.verify(out, Mockito.times(1)).close();
		Assert.assertTrue(protocol.isClosed());
	}

	@Test
	public void nextPacket() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		PacketType packetType = Mockito.mock(PacketType.class);
		Mockito.when(packetType.getTypePath()).thenReturn("a");
		factory.addPacketType(packetType);

		PipedOutputStream inputWriter = new PipedOutputStream();
		InputStream in = new BufferedInputStream(new PipedInputStream(inputWriter));
		OutputStream out = Mockito.mock(OutputStream.class);

		inputWriter.write(new byte[]
		{
				// Header
				0x00, 0x03,

				// Packet Name
				0x01, 0x61,

				// Packet
				0x0A, 0x0B, 0x0C,
		});

		protocol.init(in, out);
		protocol.next();
		protocol.close();

		Mockito.verify(listener).onPacketRecieved(Mockito.any());
	}

	@Test
	public void nextPacket_UnknownType() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		PipedOutputStream inputWriter = new PipedOutputStream();
		InputStream in = new BufferedInputStream(new PipedInputStream(inputWriter));
		OutputStream out = Mockito.mock(OutputStream.class);

		inputWriter.write(new byte[]
		{
				// Header
				0x00, 0x03,

				// Packet Name
				0x01, 0x61,

				// Packet
				0x0A, 0x0B, 0x0C,
		});

		protocol.init(in, out);
		protocol.next();
		protocol.close();

		Mockito.verify(listener, Mockito.never()).onPacketRecieved(Mockito.any());
	}

	@Test(expected = IllegalStateException.class)
	public void nextPacket_AlreadyClosed() throws IOException
	{
		PacketPool pool = new PacketPool();
		DefaultPacketFactory factory = new DefaultPacketFactory();
		PacketListener listener = Mockito.mock(PacketListener.class);
		TCPChannel sender = Mockito.mock(TCPChannel.class);
		PacketProtocol protocol = new PacketProtocol(pool, factory, listener, sender);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		protocol.init(in, out);
		protocol.close();
		protocol.next();
	}
}
