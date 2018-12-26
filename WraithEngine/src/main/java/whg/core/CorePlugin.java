package whg.core;

import net.whg.we.main.Plugin;
import net.whg.we.rendering.Graphics;
import net.whg.we.utils.Log;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;

/**
 * This is the core plugin loader for WraithEngine. It is in charge of loading
 * and sending events between plugins.
 *
 * @author TheDudeFromCI
 */
public class CorePlugin implements Plugin
{
	public static final String RENDERING_EVENTCALLER = "Core Rendering";
	public static final String PHYSICS_EVENTCALLER = "Core Physics";

	private GraphicsThread _graphicsThread;
	private PhysicsThread _physicsThread;
	private QueuedWindow _window;

	@Override
	public String getPluginName()
	{
		return "Core";
	}

	@Override
	public void initPlugin()
	{
		_graphicsThread = new GraphicsThread(this);
		_physicsThread = new PhysicsThread();

		_graphicsThread.build();
	}

	public QueuedWindow getWindow()
	{
		return _window;
	}

	public Graphics getGraphics()
	{
		return _graphicsThread.getGraphics();
	}

	@Override
	public void enablePlugin()
	{
		Log.indent();
		_window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW).setName("Untitled Project")
				.setResizable(false).setSize(640, 480).setVSync(false)
				.setListener(new DefaultWindowListener()).setGraphicsEngine(getGraphics()).build();
		Log.unindent();

		_graphicsThread.start();
		_physicsThread.start();
	}

	@Override
	public int getPriority()
	{
		return 10000;
	}
}
