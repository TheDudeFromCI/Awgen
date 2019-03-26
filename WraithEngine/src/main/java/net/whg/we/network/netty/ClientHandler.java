package net.whg.we.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String>
{
	private Client _client;

	public ClientHandler(Client client)
	{
		_client = client;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
	{
		synchronized (_client._list)
		{
			_client._list.add(msg);
		}
	}
}
