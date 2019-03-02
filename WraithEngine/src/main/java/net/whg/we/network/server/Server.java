package net.whg.we.network.server;

/**
 * Represents a server which handles accept and communicating with clients. This
 * class should handle it's own threading, and should not block the main thread.
 * All methods in this class should only be called from the main thread.
 *
 * @author TheDudeFromCI
 */
public interface Server
{
	/**
	 * Starts the server. If the server is already running, nothing happens.
	 */
	void startServer();

	/**
	 * Stops the server. If the server is not currently running, nothing happens.
	 */
	void stopServer();

	/**
	 * Checks if the server is currently running.
	 *
	 * @return True if the server is currently running. False otherwise.
	 */
	boolean isRunning();

	/*
	 * Gets the current port the server will be started on. If the server is already
	 * running, this is the port the server is currently running on.
	 */
	int getPort();

	/**
	 * Sets the port the server will be started on. If the server is already
	 * running, nothing happens.
	 *
	 * @param port
	 *            - The port the server will be started on.
	 */
	void setPort(int port);

	/**
	 * Gets a list of all connected clients.
	 *
	 * @return A list of all connected clients.
	 */
	ConnectedClientList getClientList();

	/**
	 * Processes and pending packets which are currently waiting to be handled.
	 * These packets are handled in the order in which they are recieved. Packets
	 * from the same client will always be handled in the order in which they are
	 * sent.
	 */
	void handlePackets();
}
