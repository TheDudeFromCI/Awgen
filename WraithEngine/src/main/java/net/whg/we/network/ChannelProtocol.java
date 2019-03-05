package net.whg.we.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a protocol for sending and recieving information through a socket
 * and how that information is processed.
 *
 * @author TheDudeFromCI
 */
public interface ChannelProtocol extends Closeable
{
	/**
	 * Initializes the protocol with the incoming and outgoing streams.
	 *
	 * @param in
	 *            - The stream of bytes being recieved.
	 * @param out
	 *            - The stream of bytes being sent.
	 */
	void init(InputStream in, OutputStream out);

	/**
	 * Blocks until the next packet of information is recieved and processed.
	 *
	 * @throws IOException
	 *             If an error occurs while waiting for the information to be read.
	 */
	void next() throws IOException;

	/**
	 * Called when the connected is closed.
	 */
	void onDisconnected();

	/**
	 * Checks if the connection has been closed.
	 * 
	 * @return True if the connection has been closed, false if the connection is
	 *         still open.
	 */
	boolean isClosed();
}
