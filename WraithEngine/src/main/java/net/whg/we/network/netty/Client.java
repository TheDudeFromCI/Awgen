package net.whg.we.network.netty;

import javax.net.ssl.SSLException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.whg.we.network.multiplayer.ClientEvent;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.utils.logging.Log;

public class Client
{
	private final String _ip;
	private final int _port;

	private Channel _channel;

	private PacketManagerHandler _packetManager;
	private ClientEvent _event;

	public Client(String ip, int port, PacketManagerHandler packetManager, ClientEvent event)
	{
		_ip = ip;
		_port = port;
		_packetManager = packetManager;
		_event = event;
	}

	public void start()
	{
		if (!isClosed())
			throw new IllegalStateException("Socket already running!");

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
						.handler(new ClientChannelInitializer(sslCtx, Client.this, _packetManager));

				_channel = b.connect(_ip, _port).sync().channel();
				_event.onConnect();

				while (_channel.isOpen())
					sleepSlient();
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
				_event.onDisconnect();
				group.shutdownGracefully();

				_channel = null;
			}
		});
		thread.setDaemon(true);
		thread.setName("client_channel");
		thread.start();

		while (_channel == null)
			sleepSlient();
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
		Log.trace("Shutting down socket channels.");

		if (isClosed())
			return;

		_channel.close();
	}

	public void send(Packet msg)
	{
		_channel.writeAndFlush(msg);
	}

	public boolean isClosed()
	{
		return _channel == null || !_channel.isOpen();
	}
}
