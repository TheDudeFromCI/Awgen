package net.whg.we.scene;

import net.whg.we.event.EventCaller;
import net.whg.we.utils.Log;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.rendering.GraphicsPipeline;

public class WindowedGameLoop implements GameLoop
{
	private boolean _isRunning;
	private UpdateEventCaller _updateListener = new UpdateEventCaller();
	private GraphicsPipeline _graphicsPipeline;

	public WindowedGameLoop(ResourceLoader resourceLoader)
	{
		_graphicsPipeline = new GraphicsPipeline(resourceLoader);
	}

	@Override
	public void run()
	{
		if (_isRunning)
		{
			Log.warn("Cannot run game loop! Already running!");
			return;
		}

		_isRunning = true;

		try
		{
			_graphicsPipeline.run();
		}
		catch(Exception exception)
		{
			Log.fatalf("Uncaught exception in game loop!", exception);
		}
		finally
		{
			_isRunning = false;
		}
	}

	@Override
	public EventCaller<UpdateListener> getUpdateEvent()
	{
		return _updateListener;
	}

	public GraphicsPipeline getGraphicsPipeline()
	{
		return _graphicsPipeline;
	}
}
