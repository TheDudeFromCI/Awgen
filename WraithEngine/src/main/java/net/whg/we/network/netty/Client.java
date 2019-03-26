package net.whg.we.network.netty;

import java.util.ArrayList;
import javax.net.ssl.SSLException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.whg.we.utils.logging.Log;

public class Client
{
	private final String _ip;
	private final int _port;
	private boolean _running = true;
	public ArrayList<String> _list = new ArrayList<>();

	public Client(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}

	public void start()
	{
		Thread thread = new Thread(() ->
		{
			Log.info("Starting client thread.");

			EventLoopGroup group = new NioEventLoopGroup();

			try
			{
				SslContext sslCtx = SslContextBuilder.forClient()
						.trustManager(InsecureTrustManagerFactory.INSTANCE).build();

				Bootstrap b = new Bootstrap();
				b.group(group).channel(NioSocketChannel.class)
						.handler(new ClientChannelInitializer(sslCtx, Client.this));

				Channel ch = b.connect(_ip, _port).sync().channel();

				ChannelFuture lastWriteFuture = null;

				for (int i = 0; i < 10; i++)
					lastWriteFuture = ch.writeAndFlush(i + ") Hello\n");

				while (_running)
				{
					try
					{
						Thread.sleep(1);
					}
					catch (InterruptedException e)
					{
					}
				}

				lastWriteFuture = ch.writeAndFlush("bye\n");
				ch.closeFuture().sync();

				if (lastWriteFuture != null)
					lastWriteFuture.sync();
			}
			catch (InterruptedException e)
			{
				Log.errorf("Client socket interuppted!", e);
			}
			catch (SSLException e)
			{
				Log.errorf("Failed to build SSL context!", e);
			}
			finally
			{
				Log.info("Shutting down client thread.");
				group.shutdownGracefully();
			}
		});
		thread.setDaemon(true);
		thread.setName("client_main");
		thread.start();
	}

	public String getHostIP()
	{
		return _ip;
	}

	public int getPort()
	{
		return _port;
	}

	public void stop()
	{
		_running = false;
	}
}
