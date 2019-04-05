package net.whg.frameworks.network.multiplayer;

import net.whg.frameworks.logging.Log;
import net.whg.frameworks.network.netty.Server;
import net.whg.frameworks.network.packet.PacketHandler;
import net.whg.frameworks.network.packet.PacketManagerHandler;
import net.whg.we.scene.GameState;
import net.whg.we.scene.ServerGameState;

public class MultiplayerServer implements NetworkHandler
{
	public static final int DEFAULT_PORT = 23423;

	private Server _server;
	private PacketManagerHandler _packetManager;
	private ServerEvent _event;

	public MultiplayerServer()
	{
		_event = new ServerEvent();
		_event.addListener(new MultiplayerServerListener());

		PacketHandler handler = new DefaultPacketHandler(false);
		_packetManager = PacketManagerHandler.createPacketManagerHandler(handler, true);
	}

	@Override
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
		_server = new Server(port, _packetManager, _event);
		_server.start();
	}

	public void stopServer()
	{
		Log.info("Closing multiplayer server.");

		if (!isRunning())
		{
			Log.indent();
			Log.debug("Socket already closed!");
			Log.unindent();
			return;
		}

		_server.stop();
		_server = null;
	}

	public ServerEvent getEvent()
	{
		return _event;
	}

	@Override
	public void processPackets(GameState gameState)
	{
		if (_event.getGameState() == null)
			_event.setGameState((ServerGameState) gameState);

		if (_packetManager.processor().getPacketHandler().getGameState() == null)
			_packetManager.processor().getPacketHandler().setGameState(gameState);

		_event.handlePendingEvents();
		_packetManager.processor().handlePackets();
	}
}
