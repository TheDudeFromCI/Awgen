package net.whg.we.network;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;

/**
 * Represents a passive, TCP, server connection.
 *
 * @author TheDudeFromCI
 */
public interface TCPSocket extends Closeable
{
	/**
	 * Opens the connection on the given port, if not already open.
	 * 
	 * @param port
	 *            - The port to open the server socket on.
	 * @throws IOException
	 *             - If an error occurs while attempting to open this server socket.
	 */
	void open(int port) throws IOException;

	/**
	 * Waits for the next incoming client to connect to this server, and returns an
	 * instance of the client socket as a TCPChannel. This method blocks until a
	 * connection is made.
	 *
	 * @return The next channel that has connected to this server socket.
	 * @throws IOException
	 *             - If an error has occured while connecting to a client, or while
	 *             waiting for a client.
	 * @throws SocketException
	 *             - If the connection is forcefully closed while waiting for a
	 *             client.
	 */
	TCPChannel nextChannel() throws IOException;
}
