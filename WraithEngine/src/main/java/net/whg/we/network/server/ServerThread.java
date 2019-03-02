package net.whg.we.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import net.whg.we.utils.logging.Log;

public class ServerThread
{
	private Object LOCK = new Object();

	private Thread _thread;
	private boolean _running;
	private ServerSocket _serverSocket;
	private int _port;
	private ClientHandler _clientHandler;

	public ServerThread(int port, ClientHandler clientHandler)
	{
		_port = port;
		_clientHandler = clientHandler;
	}

	public int getPort()
	{
		return _port;
	}

	public void start()
	{
		Log.info("Starting a server thread.");

		synchronized (LOCK)
		{
			if (_thread != null)
			{
				Log.warn("Server thread is already running.");
				return;
			}

			_running = true;

			_thread = new Thread(() ->
			{
				try
				{
					_serverSocket = new ServerSocket(_port);
					Log.trace("Initialized server thread.");

					while (_running)
					{
						Log.trace("Waiting for connection.");

						try
						{
							Socket socket = _serverSocket.accept();

							Log.infof("A client has connected to the server. IP: %s",
									socket.getInetAddress());

							new ClientConnection(socket, _clientHandler);
						}
						catch (SocketException e)
						{
							_running = false;
							Log.info("Server socket has been forcefully closed.");
						}
						catch (Exception e)
						{
							Log.errorf("There has been an internal error in the server socket!", e);
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
			});

			_thread.setDaemon(true);
			_thread.setName("Server");
			_thread.start();

			Log.debug("The server thread has been started.");
		}
	}

	public void stop()
	{
		Log.info("Closing server thread.");

		if (_thread == null)
		{
			Log.warn("Server thread is not currently open.");
			return;
		}

		if (Log.getLogLevel() == Log.TRACE)
			Log.indent();

		synchronized (LOCK)
		{
			_running = false;

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

			Log.trace("Final memory clean ups.");
			_serverSocket = null;
			_thread = null;
		}

		if (Log.getLogLevel() == Log.TRACE)
			Log.unindent();
	}
}
