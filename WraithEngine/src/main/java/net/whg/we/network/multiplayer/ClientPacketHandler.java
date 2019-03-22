package net.whg.we.network.multiplayer;

import net.whg.we.main.GameState;
import net.whg.we.network.packet.PacketHandler;

public class ClientPacketHandler implements PacketHandler
{
    private GameState _gameState;

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
}
