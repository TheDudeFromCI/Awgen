package network_handling;

import java.io.IOException;
import java.net.SocketException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mockito;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.server.ConnectedClientList;
import net.whg.we.network.server.ServerClientAcceptor;
import net.whg.we.network.server.ServerProtocol;
import net.whg.we.network.server.TCPSocket;

public class ServerClientAcceptorTest
{
	@Rule
	public Timeout globalTimeout = Timeout.seconds(2);

	@Test
	public void run() throws IOException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		TCPChannel client = Mockito.mock(TCPChannel.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		Mockito.when(socket.nextChannel()).thenReturn(client).thenThrow(new SocketException());

		ServerClientAcceptor acceptor = new ServerClientAcceptor(socket, 123, protocol, clientList);
		acceptor.run();

		Mockito.verify(socket).open(123);
		Mockito.verify(protocol).openChannelProtocol(client);
		Mockito.verify(socket).close();
	}

	@Test
	public void run_WriteException() throws IOException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		Mockito.when(socket.nextChannel()).thenThrow(new IOException());

		ServerClientAcceptor acceptor = new ServerClientAcceptor(socket, 123, protocol, clientList);
		acceptor.run();

		Mockito.verify(socket).close();
	}

	@Test
	public void run_failedToClose() throws IOException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		Mockito.when(socket.nextChannel()).thenThrow(new SocketException());
		Mockito.doThrow(new IOException()).when(socket).close();

		ServerClientAcceptor acceptor = new ServerClientAcceptor(socket, 123, protocol, clientList);
		acceptor.run();

		Mockito.verify(socket).close();
	}

	@Test
	public void run_FailedToOpen() throws IOException
	{
		TCPSocket socket = Mockito.mock(TCPSocket.class);
		ServerProtocol protocol = Mockito.mock(ServerProtocol.class);
		ConnectedClientList clientList = Mockito.mock(ConnectedClientList.class);

		Mockito.doThrow(new IOException()).when(socket).open(Mockito.anyInt());

		ServerClientAcceptor acceptor = new ServerClientAcceptor(socket, 123, protocol, clientList);
		acceptor.run();

		Mockito.verify(socket).close();
	}
}
