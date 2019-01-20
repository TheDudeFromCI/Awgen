package net.whg.we.main;

public class GameState
{
	private GraphicsPipeline _graphics;

	public GameState(ResourceLoader resourceLoader)
	{
		_graphics = new GraphicsPipeline(resourceLoader);
	}

	public void run()
	{
		_graphics.run();
	}
}
