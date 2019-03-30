package net.whg.we.network.multiplayer;

import net.whg.we.network.netty.Client;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.scene.GameState;
import net.whg.we.utils.logging.Log;

public class MultiplayerClient implements NetworkHandler
{
	private String _username;
	private String _token;

	private Client _client;
	private PacketManagerHandler _packetManager;
	private ClientEvent _event;
	private boolean _loggedIn;

	public MultiplayerClient(String username, String token)
	{
		_event = new ClientEvent(this);
		_event.addListener(new MultiplayerClientListener());

		_username = username;
		_token = token;

		PacketHandler handler = new DefaultPacketHandler(true);
		_packetManager = PacketManagerHandler.createPacketManagerHandler(handler, true);
	}

	@Override
	public boolean isRunning()
	{
		return _client != null && !_client.isClosed();
	}

	public void startClient(String ip)
	{
		startClient(ip, MultiplayerServer.DEFAULT_PORT);
	}

	public void startClient(String ip, int port)
	{
		if (isRunning())
			throw new IllegalStateException("Server is already running!");

		Log.infof("Opening multiplayer client on ip %s, port %d.", ip, port);
		_loggedIn = false;
		_client = new Client(ip, port, _packetManager, _event);
		_client.start();
	}

	public boolean isLoggedIn()
	{
		return _loggedIn;
	}

	public void login()
	{
		if (_loggedIn)
			throw new IllegalStateException("Already logged in!");
		_loggedIn = false;

		Log.infof(
				"Successfully connected to server. Sending handshake packet now. Username: %s, Token: %s",
				_username, _token);

		Packet packet = newPacket("auth.handshake");
		((HandshakePacket) packet.getPacketType()).build(packet, _username, _token);
		sendPacket(packet);
	}

	public PacketManagerHandler getPacketManager()
	{
		return _packetManager;
	}

	public void stopClient()
	{
		Log.debug("Closing client socket.");

		if (!isRunning())
		{
			Log.indent();
			Log.debug("Socket already closed!");
			Log.unindent();
			return;
		}

		_client.stop();
		_client = null;
		_loggedIn = false;
	}

	public Packet newPacket(String type)
	{
		return _packetManager.newPacket(type);
	}

	public void sendPacket(Packet packet)
	{
		if (!isRunning())
			throw new IllegalStateException("Client socket already closed!");

		_client.send(packet);
	}

	public ClientEvent getEvent()
	{
		return _event;
	}

	public String getUsername()
	{
		return _username;
	}

	public String getToken()
	{
		return _token;
	}

	@Override
	public void processPackets(GameState gameState)
	{
		if (_packetManager.processor().getPacketHandler().getGameState() == null)
			_packetManager.processor().getPacketHandler().setGameState(gameState);

		_event.handlePendingEvents();
		_packetManager.processor().handlePackets();
	}
}
