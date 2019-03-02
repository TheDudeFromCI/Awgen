package net.whg.we.network.server;

import net.whg.we.network.PacketFactory;
import net.whg.we.network.PacketPool;
import net.whg.we.network.PacketProcessor;

public class DefaultServer implements Server
{
	public static final int DEFAULT_PORT = 45634;

	private int _port = DEFAULT_PORT;
	private boolean _running;
	private ServerThread _serverThread;
	private ConnectedClientList _clientList;
	private ClientHandler _clientHandler;
	private PacketFactory _packetFactory;
	private PacketPool _packetPool;
	private PacketProcessor _packetProcessor;

	public DefaultServer(PacketFactory packetFactory)
	{
		_packetFactory = packetFactory;
	}

	@Override
	public void startServer()
	{
		if (isRunning())
			return;

		_clientList = new ConnectedClientList();
		_clientHandler = new ClientHandler(_clientList, _packetFactory);
		_packetPool = new PacketPool();
		_packetProcessor = new PacketProcessor(_packetPool);
		_serverThread = new ServerThread(_port, _clientHandler, _packetPool);
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

	@Override
	public void handlePackets()
	{
		_packetProcessor.handlePackets();
	}
}
