package net.whg.we.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketListener;
import net.whg.we.network.packet.PacketPool;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext _sslCtx;
	private PacketPool _packetPool;
	private PacketFactory _packetFactory;
	private PacketListener _packetListener;

	public ServerChannelInitializer(SslContext sslCtx, PacketPool packetPool,
			PacketFactory packetFactory, PacketListener packetListener)
	{
		_sslCtx = sslCtx;

		_packetPool = packetPool;
		_packetFactory = packetFactory;
		_packetListener = packetListener;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast(_sslCtx.newHandler(ch.alloc()));
		pipeline.addLast(new PacketDecoder(_packetPool, _packetFactory, _packetListener));
		pipeline.addLast(new PacketEncoder());
	}
}
