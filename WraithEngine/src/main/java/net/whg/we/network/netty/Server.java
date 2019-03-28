package net.whg.we.network.netty;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import net.whg.we.network.multiplayer.ServerEvent;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.utils.logging.Log;

public class Server
{
	private final int _port;

	private PacketManagerHandler _packetManager;
	private ServerEvent _event;

	private Channel _channel;

	public Server(int port, PacketManagerHandler packetManager, ServerEvent event)
	{
		_port = port;
		_packetManager = packetManager;
		_event = event;
	}

	public void start()
	{
		Thread thread = new Thread(() ->
		{
			Log.info("Starting server thread.");

			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup();

			try
			{
				SelfSignedCertificate ssc = new SelfSignedCertificate();
				SslContext sslCtx =
						SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();

				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
						.option(ChannelOption.SO_BACKLOG, 128)
						.childHandler(new ServerChannelInitializer(sslCtx, _packetManager, _event))
						.childOption(ChannelOption.SO_KEEPALIVE, true);

				_channel = b.bind(_port).sync().channel();
				_event.onServerStarted();

				while (_channel.isOpen())
					sleepSlient();
			}
			catch (InterruptedException e)
			{
				Log.errorf("Server socket interuppted!", e);
			}
			catch (CertificateException | SSLException e)
			{
				Log.errorf("Failed to build SSL context!", e);
				_event.onServerFailedToStart(_port);
			}
			finally
			{
				Log.info("Shutting down server thread.");
				_event.onServerStopped();
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();

				_channel = null;
			}
		});

		thread.setDaemon(true);
		thread.setName("server_channel");
		thread.start();
	}

	private void sleepSlient()
	{
		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
		}
	}

	public void stop()
	{
		Log.trace("Shutting down socket channels.");

		if (isClosed())
			return;

		_channel.close();
	}

	public boolean isClosed()
	{
		return _channel == null || !_channel.isOpen();
	}
}
