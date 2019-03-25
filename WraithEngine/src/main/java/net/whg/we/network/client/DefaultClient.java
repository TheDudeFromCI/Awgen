package net.whg.we.network.client;

import java.io.IOException;
import java.net.Socket;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.Connection;
import net.whg.we.network.DefaultTCPChannel;
import net.whg.we.network.TCPChannel;

/**
 * A default implementation of the Client interface.
 *
 * @author TheDudeFromCI
 */
public class DefaultClient
{
	private ClientEvent _event;
	private Connection _connection;

	/**
	 * Creates a new default client instance with the set protocol.
	 *
	 * @param protocol
	 */
	public DefaultClient(ChannelProtocol protocol, String ip, int port) throws IOException
	{
		TCPChannel channel = new DefaultTCPChannel(new Socket(ip, port), true);
		_event = new ClientEvent(this);
		_connection = new Connection(channel, protocol, _event);
	}

	public void close()
	{
		_connection.close();
	}

	public boolean isClosed()
	{
		return _connection.isClosed();
	}

	public ChannelProtocol getProtocol()
	{
		return _connection.getProtocol();
	}

	public ClientEvent getEvents()
	{
		return _event;
	}

	public void update()
	{
		_connection.update();
	}
}
