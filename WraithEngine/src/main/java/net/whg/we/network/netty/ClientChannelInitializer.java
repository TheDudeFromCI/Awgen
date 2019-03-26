package net.whg.we.network.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
	private final SslContext _sslCtx;
	private final Client _client;

	public ClientChannelInitializer(SslContext sslCtx, Client client)
	{
		_sslCtx = sslCtx;
		_client = client;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(_sslCtx.newHandler(ch.alloc(), _client.getHostIP(), _client.getPort()));
		pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast(new StringDecoder());
		pipeline.addLast(new StringEncoder());
		pipeline.addLast(new ClientHandler(_client));
	}
}
