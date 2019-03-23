package net.whg.we.network.multiplayer;

import net.whg.we.main.GameState;
import net.whg.we.network.packet.PacketHandler;

public class ClientPacketHandler implements PacketHandler
{
	private MultiplayerClient _client;
	private GameState _gameState;

	public ClientPacketHandler(MultiplayerClient client)
	{
		_client = client;
	}

	@Override
	public boolean isClient()
	{
		return true;
	}

	@Override
	public GameState getGameState()
	{
		return _gameState;
	}

	@Override
	public void setGameState(GameState gameState)
	{
		_gameState = gameState;
	}

	public MultiplayerClient getClient()
	{
		return _client;
	}
}
