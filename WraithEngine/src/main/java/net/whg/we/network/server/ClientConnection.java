package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;

/**
 * Represents an open connection to a client.
 *
 * @author TheDudeFromCI
 */
public class ClientConnection
{
	private ChannelProtocol _protocol;
	private ClientConnectionThread _connection;

	/**
	 * Creates a new client connection handler. This method will automatically
	 * create a background thread to handling reading and writing to the TCPChannel
	 * based on the given protocol.
	 *
	 * @param socket
	 *            - The socket this client wraps around.
	 * @param protocol
	 *            - The protocol for handling incoming and outgoing information.
	 * @throws IOException
	 *             If an error occurs while open getting the input or output stream
	 *             for the given socket.
	 */
	public ClientConnection(TCPChannel socket, ChannelProtocol protocol) throws IOException
	{
		_protocol = protocol;
		_connection = new ClientConnectionThread(socket, protocol);
	}

	/**
	 * Checks if the socket connection is currently closed.
	 *
	 * @return True if the connection has been closed. False otherwise.
	 */
	public boolean isClosed()
	{
		return _connection != null;
	}

	/**
	 * Force closes the connection.
	 */
	public void close()
	{
		if (isClosed())
			return;

		_connection.close();
		_connection = null;
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}
}
