package whg.core;

import net.whg.we.event.EventManager;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.GraphicsFactory;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Input;
import net.whg.we.utils.Time;

class GraphicsThread
{
	private CorePlugin _core;
	private Graphics _graphics;
	private RenderingEventCaller _renderEvents;

	GraphicsThread(CorePlugin core)
	{
		_core = core;
	}

	void build()
	{
		_graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		_renderEvents = new RenderingEventCaller(_core);

		EventManager.registerEventCaller(_renderEvents);
	}

	void start()
	{
		Thread thread = new Thread(() ->
		{
			_renderEvents.onGraphicsInit();

			while (true)
			{
				Time.updateTime();
				FPSLogger.logFramerate();

				_renderEvents.onPrepareRender();
				_renderEvents.onCullScene();
				_renderEvents.onFinalPrepareRender();
				_renderEvents.onRender();

				// End frame
				Input.endFrame();
				if (_core.getWindow().endFrame())
					break;
			}

			_renderEvents.onGraphicsDispose();
			_graphics.dispose();
		});

		thread.setName("render");
		thread.setDaemon(false);
		thread.start();
	}

	Graphics getGraphics()
	{
		return _graphics;
	}
}
