package net.whg.we.network;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents an open connection between a server and a client. All methods in
 * this class may be called from any thread.
 *
 * @author TheDudeFromCI
 */
public interface TCPChannel
{
	/**
	 * Gets the IP address of the client or server on the other end of this channel.
	 *
	 * @return The IP address.
	 */
	IPAddress getIP();

	/**
	 * Gets the output stream for sending out information to other side of the
	 * connection.
	 *
	 * @return The open outut stream for sending out information, or null if the
	 *         channel is closed.
	 */
	OutputStream getOutputStream();

	/**
	 * Gets the input stream for reading information being recived from the other
	 * side of the connection.
	 *
	 * @return The open input stream for reading incoming information, or null if
	 *         the channel is closed.
	 */
	InputStream getInputStream();

	/**
	 * Checks if the computer running this software represents the client side of
	 * the connection.
	 *
	 * @return True if this channel represents a client that is connected to a
	 *         server, false if this channel represents a server that recieved a
	 *         connection from a client.
	 */
	boolean isClient();

	/**
	 * Closes this socket connection.
	 */
	void close();

	/**
	 * Checks if the channel has been closed.
	 *
	 * @return True if the channel is closed, false if the channel is still open.
	 */
	boolean isClosed();
}
