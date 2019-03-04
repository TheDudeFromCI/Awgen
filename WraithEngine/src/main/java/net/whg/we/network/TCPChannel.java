package net.whg.we.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents an open connection between a server and a client.
 *
 * @author TheDudeFromCI
 */
public interface TCPChannel extends Closeable
{
	/**
	 * Gets the IP address of the client as a string.
	 *
	 * @return The IP address of the client as a string.
	 */
	String getIPString();

	/**
	 * Gets the output stream for sending out information to other side of the
	 * connection.
	 *
	 * @return The open outut stream for sending out information.
	 * @throws IOException
	 *             - If an error occurs while attempting to send out information.
	 */
	OutputStream getOutputStream() throws IOException;

	/**
	 * Gets the input stream for reading information being recived from the other
	 * side of the connection.
	 *
	 * @return The open input stream for reading incoming information.
	 * @throws IOException
	 *             - If an error occurs while waiting for information to be
	 *             received.
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * Checks if the computer running this software represents the client side of
	 * the connection.
	 * 
	 * @return True if this channel represents a client that is connected to a
	 *         server, false if this channel represents a server that recieved a
	 *         connection from a client.
	 */
	boolean isClient();
}
