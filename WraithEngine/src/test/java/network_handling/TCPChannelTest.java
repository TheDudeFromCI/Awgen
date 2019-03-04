package network_handling;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.network.DefaultTCPChannel;

public class TCPChannelTest
{
	@Test
	public void close() throws IOException
	{
		Socket socket = Mockito.mock(Socket.class);
		Mockito.when(socket.isClosed()).thenReturn(false);

		DefaultTCPChannel channel = new DefaultTCPChannel(socket, true);

		channel.close();
		Mockito.verify(socket).close();

		// Should do nothing if already closed.
		channel.close();

		Assert.assertTrue(channel.isClosed());
	}

	@Test
	public void isClosed_InternallClosed()
	{
		Socket socket = Mockito.mock(Socket.class);
		Mockito.when(socket.isClosed()).thenReturn(true);

		@SuppressWarnings("resource")
		DefaultTCPChannel channel = new DefaultTCPChannel(socket, true);

		Assert.assertTrue(channel.isClosed());
	}

	@SuppressWarnings("resource")
	@Test
	public void isClient()
	{
		Socket socket = Mockito.mock(Socket.class);

		Assert.assertTrue(new DefaultTCPChannel(socket, true).isClient());
		Assert.assertFalse(new DefaultTCPChannel(socket, false).isClient());
	}

	@Test
	public void getIP()
	{
		Socket socket = Mockito.mock(Socket.class);
		InetAddress ip = Mockito.mock(InetAddress.class);

		Mockito.when(socket.getInetAddress()).thenReturn(ip);
		Mockito.when(ip.toString()).thenReturn("168.456.185.38");

		@SuppressWarnings("resource")
		DefaultTCPChannel channel = new DefaultTCPChannel(socket, true);
		Assert.assertEquals("168.456.185.38", channel.getIP().toString());
	}

	@Test
	public void getStreams() throws IOException
	{
		Socket socket = Mockito.mock(Socket.class);
		Mockito.when(socket.isClosed()).thenReturn(false);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		Mockito.when(socket.getInputStream()).thenReturn(in);
		Mockito.when(socket.getOutputStream()).thenReturn(out);

		@SuppressWarnings("resource")
		DefaultTCPChannel channel = new DefaultTCPChannel(socket, true);

		Assert.assertEquals(in, channel.getInputStream());
		Assert.assertEquals(out, channel.getOutputStream());
	}

	@Test
	public void getStreams_Closed() throws IOException
	{
		Socket socket = Mockito.mock(Socket.class);
		Mockito.when(socket.isClosed()).thenReturn(true);

		InputStream in = Mockito.mock(InputStream.class);
		OutputStream out = Mockito.mock(OutputStream.class);

		Mockito.when(socket.getInputStream()).thenReturn(in);
		Mockito.when(socket.getOutputStream()).thenReturn(out);

		@SuppressWarnings("resource")
		DefaultTCPChannel channel = new DefaultTCPChannel(socket, true);

		Assert.assertNull(channel.getInputStream());
		Assert.assertNull(channel.getOutputStream());
	}
}
