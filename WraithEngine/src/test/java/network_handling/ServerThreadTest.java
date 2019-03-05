package network_handling;

import java.io.IOException;
import java.net.SocketException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mockito;
import net.whg.we.network.server.ConnectedClientList;
import net.whg.we.network.server.ServerProtocol;
import net.whg.we.network.server.ServerThread;
import net.whg.we.network.server.TCPSocket;

public class ServerThreadTest
{
	@Rule
	public Timeout globalTimeout = Timeout.seconds(2);

	@Test
	public void getPort()
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);

		Assert.assertEquals(123, serverThread.getPort());
	}

	@Test
	public void start() throws IOException, InterruptedException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		Mockito.doThrow(new SocketException()).when(socket).nextChannel();
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);
		serverThread.start();

		Thread.sleep(100);

		Mockito.verify(socket).open(123);
	}

	@Test
	public void start_WhileOpen() throws IOException, InterruptedException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		Mockito.doThrow(new SocketException()).when(socket).nextChannel();
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);
		serverThread.start();
		Thread.sleep(100);

		serverThread.start();
		Thread.sleep(100);

		Mockito.verify(socket).open(123);
	}

	@Test
	public void start_AfterClosing() throws IOException, InterruptedException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		// This has to be an array, as primitives cannot be assigned from
		// inside of an anonymous class.
		boolean[] isSocketClosed = new boolean[1];

		Mockito.when(socket.isClosed()).then(a ->
		{
			return isSocketClosed[0];
		});

		Mockito.doAnswer(a ->
		{
			isSocketClosed[0] = true;
			return null;
		}).when(socket).close();

		Mockito.when(socket.nextChannel()).thenAnswer(a ->
		{
			for (int i = 0; i < 5000; i++)
			{
				if (isSocketClosed[0])
					throw new SocketException();

				Thread.sleep(1);
			}
			throw new IOException();
		});

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);

		isSocketClosed[0] = false;
		serverThread.start();
		Thread.sleep(100);

		serverThread.stop();
		Thread.sleep(100);

		isSocketClosed[0] = false;
		serverThread.start();
		Thread.sleep(100);

		Mockito.verify(socket, Mockito.times(2)).open(123);
		Mockito.verify(socket, Mockito.times(1)).close();
	}

	@Test
	public void stop() throws IOException, InterruptedException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		// This has to be an array, as primitives cannot be assigned from
		// inside of an anonymous class.
		boolean[] isSocketClosed = new boolean[1];

		Mockito.when(socket.isClosed()).then(a ->
		{
			return isSocketClosed[0];
		});

		Mockito.doAnswer(a ->
		{
			isSocketClosed[0] = true;
			return null;
		}).when(socket).close();

		Mockito.when(socket.nextChannel()).thenAnswer(a ->
		{
			for (int i = 0; i < 5000; i++)
			{
				if (isSocketClosed[0])
					throw new SocketException();

				Thread.sleep(1);
			}
			throw new IOException();
		});

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);
		serverThread.start();

		Thread.sleep(100);

		serverThread.stop();

		Mockito.verify(socket).close();
	}

	@Test
	public void stop_ServerNotStarted() throws IOException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);
		serverThread.stop();

		Mockito.verify(socket, Mockito.never()).open(123);
		Mockito.verify(socket, Mockito.never()).close();
	}

	@Test
	public void stop_SocketExceptionOnClose() throws IOException, InterruptedException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		// This has to be an array, as primitives cannot be assigned from
		// inside of an anonymous class.
		boolean[] isSocketClosed = new boolean[1];

		Mockito.when(socket.isClosed()).then(a ->
		{
			return isSocketClosed[0];
		});

		Mockito.doAnswer(a ->
		{
			isSocketClosed[0] = true;
			throw new IOException();
		}).when(socket).close();

		Mockito.when(socket.nextChannel()).thenAnswer(a ->
		{
			for (int i = 0; i < 5000; i++)
			{
				if (isSocketClosed[0])
					throw new SocketException();

				Thread.sleep(1);
			}
			throw new IOException();
		});

		ServerThread serverThread = new ServerThread(123, socket, protocol, clientList);
		serverThread.start();

		Thread.sleep(100);

		serverThread.stop();

		Mockito.verify(socket).close();
	}
}
