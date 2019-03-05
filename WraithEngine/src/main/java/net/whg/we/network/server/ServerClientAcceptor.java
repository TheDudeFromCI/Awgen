package net.whg.we.network.server;

import java.io.IOException;
import java.net.SocketException;
import net.whg.we.network.TCPChannel;
import net.whg.we.utils.logging.Log;

public class ServerClientAcceptor implements Runnable
{
	private TCPSocket _serverSocket;
	private int _port;
	private ServerProtocol _protocol;

	public ServerClientAcceptor(TCPSocket serverSocket, int port, ServerProtocol protocol)
	{
		_serverSocket = serverSocket;
		_port = port;
		_protocol = protocol;
	}

	@Override
	public void run()
	{
		try
		{
			_serverSocket.open(_port);
			Log.trace("Initialized server thread.");

			while (true)
			{
				Log.trace("Waiting for connection.");

				try
				{
					TCPChannel socket = _serverSocket.nextChannel();

					Log.infof("A client has connected to the server. IP: %s", socket.getIP());

					_protocol.openChannelProtocol(socket);
				}
				catch (SocketException e)
				{
					Log.info("Server socket has been forcefully closed.");
					break;
				}
				catch (Exception e)
				{
					Log.errorf("There has been an internal error in the server socket!", e);
					break;
				}
			}
		}
		catch (Exception e)
		{
			Log.fatalf("There has been a fatal error while opening the server socket!", e);
		}
		finally
		{
			try
			{
				_serverSocket.close();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to properly close server socket!", e);
			}
		}
	}
}
