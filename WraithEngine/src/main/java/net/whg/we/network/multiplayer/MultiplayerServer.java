package net.whg.we.network.multiplayer;

import net.whg.we.network.netty.Server;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.server_logic.connect.ServerPlayerList;
import net.whg.we.utils.logging.Log;

public class MultiplayerServer
{
	public static final int DEFAULT_PORT = 23423;

	private Server _server;
	private PacketManagerHandler _packetManager;
	private ServerPlayerList _playerList;
	private ServerPacketHandler _packetHandler;

	public MultiplayerServer()
	{
		_packetHandler = new ServerPacketHandler(this);
		_packetManager = PacketManagerHandler
				.createPacketManagerHandler(new ServerPacketHandler(this), true);
		_playerList = new ServerPlayerList(_packetManager);
	}

	public boolean isRunning()
	{
		return _server != null && !_server.isClosed();
	}

	public PacketManagerHandler getPacketManager()
	{
		return _packetManager;
	}

	public void startServer()
	{
		startServer(DEFAULT_PORT);
	}

	public void startServer(int port)
	{
		if (isRunning())
			throw new IllegalStateException("Server is already running!");

		Log.infof("Opening multiplayer server on port %d.", port);
		_server = new Server(port, _packetManager);
		_server.start();
	}

	public void stopServer()
	{
		Log.info("Closing multiplayer server.");
		_server.stop();
		_server = null;
	}

	public ServerPlayerList getPlayerList()
	{
		return _playerList;
	}

	public void updatePhysics()
	{
		_packetManager.processor().handlePackets();
	}

	public ServerPacketHandler getPacketHandler()
	{
		return _packetHandler;
	}
}
