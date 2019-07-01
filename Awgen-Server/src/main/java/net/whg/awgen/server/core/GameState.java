package net.whg.awgen.server.core;

import net.whg.awgen.lib.gameloop.GameLoop;

public class GameState
{
	private GameLoop loop;

	public GameState()
	{
		loop = new GameLoop();
	}

	public GameLoop getGameLoop()
	{
		return loop;
	}
}
