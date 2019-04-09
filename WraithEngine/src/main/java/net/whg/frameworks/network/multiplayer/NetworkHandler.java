package net.whg.frameworks.network.multiplayer;

import net.whg.we.main.GameState;

public interface NetworkHandler
{
	void processPackets(GameState gameState);

	boolean isRunning();
}
