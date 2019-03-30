package net.whg.we.network.multiplayer;

import net.whg.we.scene.GameState;

public interface NetworkHandler
{
	void processPackets(GameState gameState);

	boolean isRunning();
}
