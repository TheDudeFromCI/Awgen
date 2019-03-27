package net.whg.we.network.netty;

import java.security.cert.CertificateException;
import javax.net.ssl.SSLException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import net.whg.we.network.packet.PacketFactory;
import net.whg.we.network.packet.PacketListener;
import net.whg.we.network.packet.PacketPool;
import net.whg.we.utils.logging.Log;

public class Server
{
	private final int _port;
	private boolean _running = true;

	private PacketPool _packetPool;
	private PacketFactory _packetFactory;
	private PacketListener _packetListener;

	public Server(int port, PacketPool packetPool, PacketFactory packetFactory,
			PacketListener packetListener)
	{
		_port = port;

		_packetPool = packetPool;
		_packetFactory = packetFactory;
		_packetListener = packetListener;
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
						.handler(new LoggingHandler(LogLevel.INFO))
						.childHandler(new ServerChannelInitializer(sslCtx, _packetPool,
								_packetFactory, _packetListener))
						.childOption(ChannelOption.SO_KEEPALIVE, true);

				Channel ch = b.bind(_port).sync().channel();

				while (_running)
					sleepSlient();

				ch.closeFuture().sync();
			}
			catch (InterruptedException e)
			{
				Log.errorf("Server socket interuppted!", e);
			}
			catch (CertificateException | SSLException e)
			{
				Log.errorf("Failed to build SSL context!", e);
			}
			finally
			{
				Log.info("Shutting down server thread.");
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		});

		thread.setDaemon(true);
		thread.setName("server_main");
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
		_running = false;
	}
}
