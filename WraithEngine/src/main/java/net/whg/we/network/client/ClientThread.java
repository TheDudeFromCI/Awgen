package net.whg.we.network.client;

import java.io.IOException;
import net.whg.we.network.ChannelProtocol;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class ClientThread
{
	private Thread _thread;
	private TCPChannel _channel;
	private ChannelProtocol _protocol;

	public ClientThread(TCPChannel channel, ChannelProtocol protocol)
	{
		_channel = channel;
		_protocol = protocol;
	}

	public void start()
	{
		Log.info("Starting client thread.");

		if (_thread != null)
		{
			Log.warn("Client thread is already running.");
			return;
		}

		_thread = new Thread(new ClientServerAcceptor(_channel, _protocol));
		_thread.setDaemon(true);
		_thread.setName("Server");
		_thread.start();

		Log.debug("The client thread has been started.");
	}

	public void stop()
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
			e.printStackTrace();
		}

		try
		{
			if (_thread != null)
				_thread.join();
		}
		catch (InterruptedException e)
		{
			Log.errorf("Failed to wait for client thread to close!", e);
		}
		finally
		{
			_thread = null;
		}
	}
}
