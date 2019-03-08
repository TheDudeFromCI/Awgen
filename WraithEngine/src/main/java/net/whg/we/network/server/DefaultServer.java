package net.whg.we.network.server;

import java.io.IOException;
import java.net.SocketException;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class DefaultServer implements Server
{
	private int _port;
	private ConnectedClientList _clientList;
	private ServerProtocol _protocol;
	private ServerEvent _events;
	private TCPSocket _socket;
	private Thread _thread;

	public DefaultServer(ServerProtocol protocol, int port) throws IOException
	{
		_port = port;
		_protocol = protocol;
		_events = new ServerEvent(this);
		_clientList = new ConnectedClientList();
		_socket = new DefaultTCPSocket();

		_thread = new Thread(() ->
		{
			try
			{
				_socket.open(_port);
				Log.trace("Initialized server thread.");

				_events.onServerStarted();

				while (true)
				{
					Log.trace("Waiting for connection.");

					try
					{
						TCPChannel socket = _socket.nextChannel();

						Log.infof(
								"A client has connected to the server. IP: %s",
								socket.getIP());

						ClientConnection client =
								_protocol.openChannelProtocol(socket);
						_clientList.addClient(client);
						_events.onClientConnected(client);
					}
					catch (SocketException e)
					{
						Log.info("Server socket has been forcefully closed.");
						break;
					}
					catch (Exception e)
					{
						Log.errorf(
								"There has been an internal error in the server socket!",
								e);
						break;
					}
				}
			}
			catch (Exception e)
			{
				Log.errorf(
						"There has been a error while opening the server socket!",
						e);

				_events.onServerFailedToStart();
			}
			finally
			{
				try
				{
					_socket.close();
					_events.onServerStopped();
				}
				catch (IOException e)
				{
					Log.errorf("Failed to properly close server socket!", e);
				}
			}
		});
		_thread.setDaemon(true);
		_thread.setName("Server");
		_thread.start();
	}

	@Override
	public void stopServer()
	{
		if (_socket.isClosed())
			return;

		Log.info("Closing server thread.");

		try
		{
			_socket.close();
		}
		catch (IOException e)
		{
			Log.errorf("Failed to close server socket!", e);
		}

		try
		{
			Log.trace("Waiting until thread is fully closed.");
			_thread.join();
		}
		catch (InterruptedException e)
		{
			Log.errorf("Failed to wait for server thread to close!", e);
		}
	}

	@Override
	public boolean isRunning()
	{
		return !_socket.isClosed();
	}

	@Override
	public int getPort()
	{
		return _port;
	}

	@Override
	public ConnectedClientList getClientList()
	{
		return _clientList;
	}

	@Override
	public ServerEvent getEvents()
	{
		return _events;
	}

	public ServerProtocol getProtocol()
	{
		return _protocol;
	}
}
