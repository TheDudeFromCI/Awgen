package net.whg.we.network.multiplayer;

import net.whg.we.network.packet.PacketHandler;
import net.whg.we.scene.GameState;

public class DefaultPacketHandler implements PacketHandler
{
	private GameState _gameState;
	private boolean _client;

	public DefaultPacketHandler(boolean isClient)
	{
		_client = isClient;
	}

	@Override
	public boolean isClient()
	{
		return _client;
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
}
