package net.whg.we.network.multiplayer;

import net.whg.we.client_logic.connect.ClientPlayer;
import net.whg.we.client_logic.connect.ClientPlayerList;
import net.whg.we.network.netty.Client;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketManagerHandler;
import net.whg.we.utils.logging.Log;

public class MultiplayerClient
{
	private Client _client;
	private PacketManagerHandler _packetManager;
	private ClientPacketHandler _handler;
	private ClientPlayerList _playerList;
	private ClientPlayer _player;
	private ClientEvent _event;
	private boolean _loggedIn;

	public MultiplayerClient(String username, String token)
	{
		_handler = new ClientPacketHandler(this);
		_playerList = new ClientPlayerList();

		_event = new ClientEvent(this);
		_event.addListener(new MultiplayerClientListener());

		_player = new ClientPlayer(username, token);
		_playerList.addPlayer(_player);

		_packetManager = PacketManagerHandler.createPacketManagerHandler(_handler, true);
	}

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

		String username = _player.getUsername();
		String token = _player.getToken();

		Log.infof(
				"Successfully connected to server. Sending handshake packet now. Username: %s, Token: %s",
				username, token);

		Packet packet = newPacket("auth.handshake");
		((HandshakePacket) packet.getPacketType()).build(packet, username, token);
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

	public void updatePhysics()
	{
		_event.handlePendingEvents();
		_packetManager.processor().handlePackets();
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

	public ClientPacketHandler getPacketHandler()
	{
		return _handler;
	}

	public ClientPlayerList getPlayerList()
	{
		return _playerList;
	}

	public ClientPlayer getPlayer()
	{
		return _player;
	}

	public ClientEvent getEvent()
	{
		return _event;
	}
}
