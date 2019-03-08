package net.whg.we.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.DefaultTCPChannel;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

/**
 * A default implementation of the Client interface.
 *
 * @author TheDudeFromCI
 */
public class DefaultClient
{
	private ChannelProtocol _protocol;
	private TCPChannel _channel;
	private Thread _thread;

	/**
	 * Creates a new default client instance with the set protocol.
	 *
	 * @param protocol
	 */
	public DefaultClient(ChannelProtocol protocol, String ip, int port)
			throws IOException
	{
		_protocol = protocol;
		_channel = new DefaultTCPChannel(new Socket(ip, port), true);
		_protocol.init(_channel.getInputStream(), _channel.getOutputStream(),
				_channel);

		_thread = new Thread(() ->
		{
			try
			{
				while (true)
					_protocol.next();
			}
			catch (SocketException e)
			{
				Log.info("Client socket has been forcefully closed.");
			}
			catch (IOException e)
			{
				Log.info("The connection has been closed.");
			}
			catch (Exception e)
			{
				Log.errorf("An error has occured while handling client socket!",
						e);
			}
			finally
			{
				try
				{
					_protocol.close();
					_channel.close();
				}
				catch (IOException e)
				{
					Log.errorf(
							"An error has occured while closing client sockets!",
							e);
				}
			}
		});
		_thread.setDaemon(true);
		_thread.start();
	}

	public void close() throws IOException
	{
		if (isClosed())
			return;

		_protocol.close();
		_channel.close();

		try
		{
			if (_thread != null)
				_thread.join();
		}
		catch (InterruptedException e)
		{
			Log.errorf("Failed to wait for client thread to close!", e);
		}
	}

	public boolean isClosed()
	{
		return _channel.isClosed();
	}

	public ChannelProtocol getProtocol()
	{
		return _protocol;
	}
}
