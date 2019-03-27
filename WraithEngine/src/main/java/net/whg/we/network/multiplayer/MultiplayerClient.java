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

	public MultiplayerClient(String username, String token)
	{
		_handler = new ClientPacketHandler(this);
		_playerList = new ClientPlayerList();

		_player = new ClientPlayer(username, token);
		_playerList.addPlayer(_player);

		_packetManager = PacketManagerHandler
				.createPacketManagerHandler(new ClientPacketHandler(this), true);
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
		_client = new Client(ip, port, _packetManager);
		_client.start();
	}

	public PacketManagerHandler getPacketManager()
	{
		return _packetManager;
	}

	public void stopClient()
	{
		if (!isRunning())
			return;

		_client.stop();
		_client = null;
	}

	public void updatePhysics()
	{
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
}
