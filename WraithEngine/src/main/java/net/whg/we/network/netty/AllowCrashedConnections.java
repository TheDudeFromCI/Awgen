package net.whg.we.network.netty;

import java.io.IOException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.whg.we.utils.logging.Log;

public class AllowCrashedConnections extends ChannelInboundHandlerAdapter
{
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		ctx.close();

		// Client force closed
		if (cause instanceof IOException)
			return;

		Log.errorf("An error has occured while handling socket!", cause);
	}
}
