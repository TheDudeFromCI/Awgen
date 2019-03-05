package net.whg.we.network.client;

import java.io.IOException;
import java.net.UnknownHostException;
import net.whg.we.network.TCPChannel;

/**
 * Represents a class which can connect to a server.
 *
 * @author TheDudeFromCI
 */
public interface Client
{
	/**
	 * Attempts to open a connection to a server.
	 *
	 * @param ip
	 *            - The IP of the server to connect to.
	 * @param port
	 *            - The port of the server to connect to.
	 * @throws UnknownHostException
	 *             If the IP is invalid.
	 * @throws IOException
	 *             If an internal error occurs while connecting to the server.
	 * @return The TCPChannel for the current connnection.
	 */
	TCPChannel connect(String ip, int port) throws UnknownHostException, IOException;

	/**
	 * Force closes the connection.
	 *
	 * @throws IOException
	 *             If there is an internal error while closing the connection.
	 */
	void close() throws IOException;

	/**
	 * Checks if the connection is currently closed.
	 *
	 * @return True if the connection to the server is closed. False otherwise.
	 */
	boolean isClosed();
}
