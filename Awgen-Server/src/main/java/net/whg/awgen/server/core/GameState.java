package net.whg.awgen.server.core;

import net.whg.awgen.lib.gameloop.GameLoop;
import net.whg.stlib.scene.Scene;

public class GameState
{
	private GameLoop loop;
	private Scene scene;

	public GameState()
	{
		loop = new GameLoop();
		scene = new Scene();
	}

	public GameLoop getGameLoop()
	{
		return loop;
	}

	public Scene getScene()
	{
		return scene;
	}
}
