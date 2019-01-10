package net.whg.we.threads;

import net.whg.we.event.EventManager;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.GraphicsFactory;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;
import whg.core.CorePlugin;
import whg.core.DefaultWindowListener;

public class GraphicsThread extends ThreadInstance
{
	private CorePlugin _core;
	private QueuedWindow _window;
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
			try
			{
				Log.indent();

				_window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW)
						.setName("Untitled Project").setResizable(false).setSize(640, 480)
						.setVSync(false).setListener(new DefaultWindowListener())
						.setGraphicsEngine(getGraphics()).build();
				Log.unindent();

				_graphics.init();
				_renderEvents.onGraphicsInit();

				while (true)
				{
					try
					{
						// Calculate frame data
						Time.updateTime();
						FPSLogger.logFramerate();

						// Handle pending message queue
						handleMessages();

						// Render the scene
						_renderEvents.onPrepareRender();
						_renderEvents.onCullScene();
						_renderEvents.onFinalPrepareRender();
						_renderEvents.onRender();

						// End frame
						Input.endFrame();
						if (_window.endFrame())
						{
							_core.getThreadManager().getPhysicsThread().requestClose();
							break;
						}
					}
					catch (Exception exception)
					{
						Log.fatalf("Fatal error thrown in rendering thread!", exception);
						_window.requestClose();
						_core.getThreadManager().getPhysicsThread().requestClose();
						break;
					}
				}

				_renderEvents.onGraphicsDispose();

				ResourceDatabase.disposeAll();
				_graphics.dispose();
			}
			catch (Exception exception)
			{
				Log.fatalf("Uncaught exception through in rendering thread!", exception);
				System.exit(1);
			}
		});

		thread.setName("render");
		thread.setDaemon(false);
		initalize(thread);
		thread.start();
	}

	public Graphics getGraphics()
	{
		return _graphics;
	}

	public QueuedWindow getWindow()
	{
		return _window;
	}
}
