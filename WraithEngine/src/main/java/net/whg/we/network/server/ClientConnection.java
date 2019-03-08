package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

/**
 * Represents an open connection to a client.
 *
 * @author TheDudeFromCI
 */
public class ClientConnection
{
	private TCPChannel _socket;
	private ChannelProtocol _protocol;
	private Thread _thread;

	/**
	 * Creates a new client connection handler. This method will automatically
	 * create a background thread to handling reading and writing to the
	 * TCPChannel based on the given protocol.
	 *
	 * @param socket
	 *                     - The socket this client wraps around.
	 * @param protocol
	 *                     - The protocol for handling incoming and outgoing
	 *                     information.
	 * @throws IOException
	 *                         If an error occurs while open getting the input
	 *                         or output stream for the given socket.
	 */
	public ClientConnection(Server server, TCPChannel socket,
			ChannelProtocol protocol) throws IOException
	{
		_socket = socket;
		_protocol = protocol;
		_protocol.init(socket.getInputStream(), socket.getOutputStream(),
				socket);

		_thread = new Thread(() ->
		{
			try
			{
				while (true)
					_protocol.next();
			}
			catch (IOException e)
			{
				Log.errorf("An error has occured within the client connection!",
						e);
			}
			finally
			{
				try
				{
					_protocol.close();
					_socket.close();
				}
				catch (Exception e)
				{
					Log.errorf("Failed to close socket!", e);
				}

				server.getEvents().onClientDisconnected(ClientConnection.this);
			}
		});
		_thread.setDaemon(true);
		_thread.start();
	}

	/**
	 * Checks if the socket connection is currently closed.
	 *
	 * @return True if the connection has been closed. False otherwise.
	 */
	public boolean isClosed()
	{
		return _socket.isClosed();
	}

	/**
	 * Force closes the connection.
	 */
	public void close()
	{
		Log.info("Force closing socket connection.");

		try
		{
			_protocol.close();
			_socket.close();
		}
		catch (IOException e)
		{
			Log.errorf("Failed to close socket!", e);
		}

		try
		{
			_thread.join();
		}
		catch (InterruptedException e)
		{
			Log.errorf("Failed to wait for client connection thread to end!",
					e);
		}
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}
}
