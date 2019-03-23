package net.whg.we.network.packet;

import net.whg.we.main.GameState;

public interface PacketHandler
{
    boolean isClient();

    GameState getGameState();

    void setGameState(GameState gameState);
}
