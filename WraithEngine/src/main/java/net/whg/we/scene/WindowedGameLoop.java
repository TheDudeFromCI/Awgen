package net.whg.we.scene;

import net.whg.we.event.EventCaller;
import net.whg.we.utils.Log;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.rendering.GraphicsPipeline;
import net.whg.we.test.TestScene;
import net.whg.we.utils.Input;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Time;

public class WindowedGameLoop implements GameLoop
{
	private boolean _isRunning;
	private UpdateEventCaller _updateListener = new UpdateEventCaller();
	private GraphicsPipeline _graphicsPipeline;
	private ResourceLoader _resourceLoader;

	public WindowedGameLoop(ResourceLoader resourceLoader)
	{
		_resourceLoader = resourceLoader;
		_graphicsPipeline = new GraphicsPipeline();
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
			TestScene testScene = new TestScene(this);
			testScene.loadTestScene(_resourceLoader);

			while (true)
			{
				try
				{
					// Calculate frame data
					Time.updateTime();
					FPSLogger.logFramerate();

					_updateListener.onUpdate();

					// End frame
					Input.endFrame();
					if (_graphicsPipeline.getWindow().endFrame())
						break;
				}
				catch (Exception exception)
				{
					Log.fatalf("Fatal error thrown in main thread!", exception);
					_graphicsPipeline.requestClose();
					break;
				}
			}
		}
		catch(Exception exception)
		{
			Log.fatalf("Uncaught exception in game loop!", exception);
		}
		finally
		{
			_resourceLoader.disposeResources();
			_graphicsPipeline.dispose();
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

	@Override
	public void requestClose()
	{
		_graphicsPipeline.getWindow().requestClose();
	}
}
