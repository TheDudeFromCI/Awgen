package net.whg.we.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import net.whg.we.network.multiplayer.ServerEvent;
import net.whg.we.network.packet.PacketManagerHandler;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext _sslCtx;
	private PacketManagerHandler _packetManager;
	private ServerEvent _event;

	public ServerChannelInitializer(SslContext sslCtx, PacketManagerHandler packetManager,
			ServerEvent event)
	{
		_sslCtx = sslCtx;
		_packetManager = packetManager;
		_event = event;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();

		UserConnection userConnection = new UserConnection(ch, false);

		pipeline.addLast(new AllowCrashedConnections());
		pipeline.addLast(_sslCtx.newHandler(ch.alloc()));
		pipeline.addLast(new PacketDecoder(userConnection, _packetManager));
		pipeline.addLast(new PacketEncoder(_packetManager.pool()));
		pipeline.addLast(new ClientDisconnectHandler(_event, userConnection));

		_event.onUserConnected(userConnection);
	}
}
