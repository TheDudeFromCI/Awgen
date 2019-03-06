package net.whg.we.network.server;

public class DefaultServer implements Server
{
	public static final int DEFAULT_PORT = 45634;

	protected int _port = DEFAULT_PORT;
	protected boolean _running;
	protected ServerThread _serverThread;
	protected ConnectedClientList _clientList;
	protected ServerProtocol _protocol;

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
		_serverThread = new ServerThread(_port, new DefaultTCPSocket(), _protocol, _clientList);
		_serverThread.start();
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
