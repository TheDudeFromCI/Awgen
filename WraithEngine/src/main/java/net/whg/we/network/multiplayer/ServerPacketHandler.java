package net.whg.we.network.multiplayer;

import net.whg.we.main.GameState;
import net.whg.we.network.packet.PacketHandler;

public class ServerPacketHandler implements PacketHandler
{
    private MultiplayerServer _server;
    private GameState _gameState;

    public ServerPacketHandler(MultiplayerServer server)
    {
        _server = server;
    }

    public MultiplayerServer getServer()
    {
        return _server;
    }

    @Override
    public boolean isClient()
    {
        return false;
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
