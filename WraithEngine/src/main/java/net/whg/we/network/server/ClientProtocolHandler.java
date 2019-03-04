package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class ClientProtocolHandler implements Runnable
{
	private TCPChannel _socket;
	private ChannelProtocol _protocol;

	public ClientProtocolHandler(TCPChannel socket, ChannelProtocol protocol)
	{
		_socket = socket;
		_protocol = protocol;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
				_protocol.next();
		}
		catch (IOException e)
		{
			Log.errorf("An error has occured within the client connection!", e);
		}
		finally
		{
			try
			{
				_protocol.close();
				_socket.close();
			}
			catch (Exception e)
			{
				Log.errorf("Failed to close socket!", e);
			}
		}

		_protocol.onDisconnected();
	}
}
