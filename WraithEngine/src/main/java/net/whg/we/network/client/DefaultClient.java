package net.whg.we.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.DefaultTCPChannel;
import net.whg.we.network.TCPChannel;

/**
 * A default implementation of the Client interface.
 *
 * @author TheDudeFromCI
 */
public class DefaultClient implements Client
{
	protected TCPChannel _channel;
	protected ChannelProtocol _protocol;
	protected ClientThread _clientThread;

	/**
	 * Creates a new default client instance with the set protocol.
	 *
	 * @param protocol
	 */
	public DefaultClient(ChannelProtocol protocol)
	{
		_protocol = protocol;
	}

	@Override
	public TCPChannel connect(String ip, int port) throws UnknownHostException, IOException
	{
		_channel = new DefaultTCPChannel(new Socket(ip, port), true);
		_protocol.init(_channel.getInputStream(), _channel.getOutputStream());
		_clientThread = new ClientThread(_channel, _protocol);
		_clientThread.start();

		return _channel;
	}

	@Override
	public void close() throws IOException
	{
		_protocol.close();
		_channel.close();
	}

	@Override
	public boolean isClosed()
	{
		return _channel.isClosed();
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}
}
