package net.whg.we.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import net.whg.we.network.ChannelProtocol;

public class DefaultClient implements Client
{
	private Socket _socket;
	private ChannelProtocol _protocol;

	public DefaultClient(ChannelProtocol protocol)
	{
		_protocol = protocol;
	}

	@Override
	public void connect(String ip, int port) throws UnknownHostException, IOException
	{
		_socket = new Socket(ip, port);
		_protocol.init(_socket.getInputStream(), _socket.getOutputStream());
	}

	@Override
	public void close() throws IOException
	{
		_protocol.close();
		_socket.close();
	}

	@Override
	public boolean isClosed()
	{
		return _socket.isClosed();
	}
}
