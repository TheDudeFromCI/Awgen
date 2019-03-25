package net.whg.we.network;

import java.io.IOException;

/**
 * Represents a protocol for sending and recieving information through a socket
 * and how that information is processed.
 *
 * @author TheDudeFromCI
 */
public interface ChannelProtocol
{
	/**
	 * Initializes the protocol with the incoming and outgoing streams.
	 *
	 * @param sender
	 *            - The channel to manage.
	 */
	void init(TCPChannel sender);

	/**
	 * Blocks until the next packet of information is recieved and processed.
	 *
	 * @throws IOException
	 *             If an error occurs while waiting for the information to be read.
	 */
	void next() throws IOException;

	/**
	 * Called each physics frame.
	 *
	 * @throws IOException
	 *             If an error occurs while updating connection.
	 */
	void update() throws IOException;
}
