package net.whg.we.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketListener;
import net.whg.we.network.packet.PacketPool;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext _sslCtx;
	private final Client _client;
	private PacketPool _packetPool;
	private PacketFactory _packetFactory;
	private PacketListener _packetListener;

	public ClientChannelInitializer(SslContext sslCtx, Client client, PacketPool packetPool,
			PacketFactory packetFactory, PacketListener packetListener)
	{
		_sslCtx = sslCtx;
		_client = client;

		_packetPool = packetPool;
		_packetFactory = packetFactory;
		_packetListener = packetListener;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();

		UserConnection userConnection = new UserConnection(ch, true);

		pipeline.addLast(_sslCtx.newHandler(ch.alloc(), _client.getHostIP(), _client.getPort()));
		pipeline.addLast(
				new PacketDecoder(_packetPool, _packetFactory, _packetListener, userConnection));
		pipeline.addLast(new PacketEncoder());
	}
}
