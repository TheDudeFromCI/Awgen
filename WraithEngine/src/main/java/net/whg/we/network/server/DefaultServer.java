package net.whg.we.network.server;

public class DefaultServer implements Server
{
	public static final int DEFAULT_PORT = 45634;

	private int _port = DEFAULT_PORT;
	private boolean _running;
	private ServerThread _serverThread;
	private ConnectedClientList _clientList;
	private ServerProtocol _protocol;

	public DefaultServer(ServerProtocol protocol)
	{
		_protocol = protocol;
	}

	@Override
	public void startServer()
	{
		if (isRunning())
			return;

		_clientList = new ConnectedClientList();
		_serverThread = new ServerThread(_port, new DefaultTCPSocket(), _protocol);
	}

	@Override
	public void stopServer()
	{
		if (!isRunning())
			return;

		_running = false;
		_serverThread.stop();
		_serverThread = null;
	}

	@Override
	public boolean isRunning()
	{
		return _running;
	}

	@Override
	public int getPort()
	{
		return _port;
	}

	@Override
	public void setPort(int port)
	{
		_port = port;
	}

	@Override
	public ConnectedClientList getClientList()
	{
		return _clientList;
	}
}
