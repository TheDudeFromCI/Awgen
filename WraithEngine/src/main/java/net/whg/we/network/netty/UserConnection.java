package net.whg.we.network.netty;

import java.io.InputStream;
import java.io.OutputStream;
import io.netty.channel.socket.SocketChannel;
import net.whg.we.network.DefaultIPAddress;
import net.whg.we.network.IPAddress;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.packet.Packet;

public class UserConnection implements TCPChannel
{
	private SocketChannel _channel;
	private IPAddress _ip;
	private boolean _isClient;

	public UserConnection(SocketChannel channel, boolean isClient)
	{
		_channel = channel;
		_isClient = isClient;
	}

	public void sendPacket(Packet packet)
	{
		_channel.writeAndFlush(packet);
	}

	@Override
	public IPAddress getIP()
	{
		if (_ip == null)
			_ip = new DefaultIPAddress(_channel.localAddress().getAddress());

		return _ip;
	}

	@Override
	public OutputStream getOutputStream()
	{
		return null;
	}

	@Override
	public InputStream getInputStream()
	{
		return null;
	}

	@Override
	public boolean isClient()
	{
		return _isClient;
	}

	@Override
	public void close()
	{
		_channel.close();
	}

	@Override
	public boolean isClosed()
	{
		return _channel.isShutdown();
	}
}
