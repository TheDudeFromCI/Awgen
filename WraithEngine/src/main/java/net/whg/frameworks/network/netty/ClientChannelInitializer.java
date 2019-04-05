package net.whg.frameworks.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import net.whg.frameworks.network.packet.PacketManagerHandler;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext _sslCtx;
	private final Client _client;
	private PacketManagerHandler _packetManager;

	public ClientChannelInitializer(SslContext sslCtx, Client client,
			PacketManagerHandler packetManager)
	{
		_sslCtx = sslCtx;
		_client = client;
		_packetManager = packetManager;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();

		UserConnection userConnection = new UserConnection(ch, true);

		pipeline.addLast(new AllowCrashedConnections());
		pipeline.addLast(_sslCtx.newHandler(ch.alloc(), _client.getHostIP(), _client.getPort()));
		pipeline.addLast(new PacketDecoder(userConnection, _packetManager));
		pipeline.addLast(new PacketEncoder(_packetManager.pool()));
	}
}
