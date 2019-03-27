package net.whg.we.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import net.whg.we.network.packet.PacketManagerHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext _sslCtx;
	private PacketManagerHandler _packetManager;

	public ServerChannelInitializer(SslContext sslCtx, PacketManagerHandler packetManager)
	{
		_sslCtx = sslCtx;
		_packetManager = packetManager;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();

		UserConnection userConnection = new UserConnection(ch, false);

		pipeline.addLast(_sslCtx.newHandler(ch.alloc()));
		pipeline.addLast(new PacketDecoder(userConnection, _packetManager));
		pipeline.addLast(new PacketEncoder());
	}
}
