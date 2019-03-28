package net.whg.we.network.netty;

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
import net.whg.we.network.multiplayer.ClientEvent;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.utils.logging.Log;

public class Client
{
	private final String _ip;
	private final int _port;
	private boolean _running;

	private Channel _channel;
	private ChannelFuture _channelFuture;
	private Object _lock = new Object();

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
		if (_running)
			throw new IllegalStateException("Socket already running!");

		while (true)
		{
			synchronized (_lock)
			{
				if (_channel == null)
					break;
			}

			sleepSlient();
		}

		_running = true;

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

				synchronized (_lock)
				{
					_channel = b.connect(_ip, _port).sync().channel();
					_channelFuture = null;
				}

				_event.onConnect();

				while (_running)
					sleepSlient();

				_event.onDisconnect();

				synchronized (_lock)
				{
					_channel.closeFuture().sync();

					if (_channelFuture != null)
						_channelFuture.sync();

					_channel = null;
					_channelFuture = null;
				}
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
		_running = false;
	}

	public void send(Packet msg)
	{
		synchronized (_lock)
		{
			_channelFuture = _channel.writeAndFlush(msg);
		}
	}

	public boolean isClosed()
	{
		return !_running;
	}
}
