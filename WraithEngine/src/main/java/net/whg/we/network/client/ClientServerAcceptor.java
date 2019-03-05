package net.whg.we.network.client;

import java.io.IOException;
import java.net.SocketException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class ClientServerAcceptor implements Runnable
{
	private TCPChannel _channel;
	private ChannelProtocol _protocol;

	public ClientServerAcceptor(TCPChannel channel, ChannelProtocol protocol)
	{
		_channel = channel;
		_protocol = protocol;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				_protocol.next();
			}
			catch (SocketException e)
			{
				Log.info("Client socket has been forcefully closed.");
				break;
			}
			catch (IOException e)
			{
				Log.info("The connection has been closed.");
				break;
			}
			catch (Exception e)
			{
				Log.errorf("An error has occured while handling client socket!", e);
				break;
			}
			finally
			{
				try
				{
					if (!_protocol.isClosed())
						_protocol.close();

					if (!_channel.isClosed())
						_channel.close();
				}
				catch (IOException e)
				{
					Log.errorf("An error has occured while closing client sockets!", e);
				}
			}
		}
	}
}
