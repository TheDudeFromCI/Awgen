package net.whg.we.network.netty;

import java.net.InetAddress;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.whg.we.utils.logging.Log;

public class ServerHandler extends SimpleChannelInboundHandler<String>
{
	private static final ChannelGroup _channels =
			new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
	{
		for (Channel c : _channels)
		{
			if (c != ctx.channel())
				c.writeAndFlush(ctx.channel().remoteAddress() + "> " + msg + "\n");
			else
				c.writeAndFlush("You> " + msg + "\n");
		}

		if (msg.toLowerCase().equals("bye"))
			ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(future ->
		{
			ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + ".\n");
			ctx.writeAndFlush("Your session is protected by "
					+ ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite()
					+ " cipher suite.\n");
			_channels.add(ctx.channel());
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		Log.errorf("And error has been thrown while handling socket!", cause);
		ctx.close();
	}
}
