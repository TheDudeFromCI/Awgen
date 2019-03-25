package net.whg.we.network;

import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferOverflowException;
import net.whg.we.utils.logging.Log;

public class Connection
{
	private TCPChannel _channel;
	private ChannelProtocol _protocol;

	public Connection(TCPChannel channel, ChannelProtocol protocol, ConnectionEvent event)
	{
		_channel = channel;
		_protocol = protocol;

		Thread thread = new Thread(() ->
		{
			event.onConnect(Connection.this);
			_protocol.init(_channel);

			try
			{
				while (!_channel.isClosed())
					_protocol.next();
			}
			catch (SocketException e)
			{
				Log.info("Client socket has been forcefully closed.");
			}
			catch (IOException | BufferOverflowException | IndexOutOfBoundsException e)
			{
				Log.info("The connection has been closed from the other side.");
			}
			catch (Exception e)
			{
				Log.errorf("An error has occured while handling client socket!", e);
			}
			finally
			{
				close();
				event.onDisconnect(Connection.this);
			}
		});
		thread.setName("connnection_to-" + channel.getIP());
		thread.setDaemon(true);
		thread.start();
	}

	public boolean isClosed()
	{
		return _channel.isClosed();
	}

	public boolean isClient()
	{
		return _channel.isClient();
	}

	public void close()
	{
		if (isClosed())
			return;

		_channel.close();
	}

	public void update()
	{
		if (isClosed())
			return;

		try
		{
			_protocol.update();
		}
		catch (IOException | BufferOverflowException | IndexOutOfBoundsException e)
		{
			Log.info("The connection has been closed from the other side.");
			close();
		}
		catch (Exception e)
		{
			Log.errorf("An error has occured while attempting to update protocol!", e);
			close();
		}
	}

	public TCPChannel getChannel()
	{
		return _channel;
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}

	public IPAddress getIP()
	{
		return _channel.getIP();
	}
}
