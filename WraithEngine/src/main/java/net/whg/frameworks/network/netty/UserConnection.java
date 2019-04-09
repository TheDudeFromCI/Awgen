package net.whg.frameworks.network.netty;

import io.netty.channel.socket.SocketChannel;
import net.whg.frameworks.network.DefaultIPAddress;
import net.whg.frameworks.network.IPAddress;
import net.whg.frameworks.network.packet.Packet;

public class UserConnection
{
	private SocketChannel _channel;
	private IPAddress _ip;
	private UserState _state;

	public UserConnection(SocketChannel channel, boolean isClient)
	{
		_channel = channel;
		_state = new UserState(isClient);
	}

	/**
	 * Sends a packet message through this connection.
	 *
	 * @param packet
	 *            - The packet to send through this connection.
	 */
	public void sendPacket(Packet packet)
	{
		_channel.writeAndFlush(packet);
	}

	public IPAddress getIP()
	{
		if (_ip == null)
			_ip = new DefaultIPAddress(_channel.localAddress().getAddress());

		return _ip;
	}

	public void close()
	{
		_channel.close();
	}

	public boolean isClosed()
	{
		return _channel.isShutdown();
	}

	public UserState getUserState()
	{
		return _state;
	}
}
