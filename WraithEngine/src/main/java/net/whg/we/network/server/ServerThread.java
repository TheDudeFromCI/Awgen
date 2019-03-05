package net.whg.we.network.server;

import java.io.IOException;
import net.whg.we.utils.logging.Log;

public class ServerThread
{
	private Thread _thread;
	private TCPSocket _serverSocket;
	private int _port;
	private ServerProtocol _protocol;

	public ServerThread(int port, TCPSocket serverSocket, ServerProtocol protocol)
	{
		_port = port;
		_serverSocket = serverSocket;
		_protocol = protocol;
	}

	public int getPort()
	{
		return _port;
	}

	public void start()
	{
		Log.info("Starting a server thread.");

		if (_thread != null)
		{
			Log.warn("Server thread is already running.");
			return;
		}

		_thread = new Thread(new ServerClientAcceptor(_serverSocket, _port, _protocol));
		_thread.setDaemon(true);
		_thread.setName("Server");
		_thread.start();

		Log.debug("The server thread has been started.");
	}

	public void stop()
	{
		Log.info("Closing server thread.");

		if (_thread == null)
		{
			Log.warn("Server thread is not currently open.");
			return;
		}

		if (_serverSocket != null)
		{
			try
			{
				Log.trace("Force closing server socket.");
				_serverSocket.close();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to properly close server socket!", e);
			}
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

		_thread = null;
	}
}
