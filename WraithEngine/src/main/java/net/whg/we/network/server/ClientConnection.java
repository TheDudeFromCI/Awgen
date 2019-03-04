package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.PacketPool;
import net.whg.we.network.TCPChannel;

public class ClientConnection
{
	private ClientConnectionThread _connection;
	private PacketPool _packetPool;

	public ClientConnection(TCPChannel socket, ConnectionListener listener, PacketPool packetPool)
			throws IOException
	{
		_packetPool = packetPool;
		_connection = new ClientConnectionThread(socket, this, listener);
	}

	public boolean isClosed()
	{
		return _connection != null;
	}

	public void close()
	{
		if (isClosed())
			return;

		_connection.close();
		_connection = null;
	}

	public PacketPool getPacketPool()
	{
		return _packetPool;
	}
}
