package net.whg.we.network.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.whg.frameworks.logging.Log;
import net.whg.we.network.multiplayer.ServerEvent;

public class ClientDisconnectHandler extends ChannelInboundHandlerAdapter
{
	private ServerEvent _event;
	private UserConnection _userConnection;

	public ClientDisconnectHandler(ServerEvent event, UserConnection userConnection)
	{
		_event = event;
		_userConnection = userConnection;
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
	{
		_event.onUserDisconnected(_userConnection);
		Log.debugf("Client %s has closed the connection.", _userConnection.getIP());

		super.handlerRemoved(ctx);
	}
}
