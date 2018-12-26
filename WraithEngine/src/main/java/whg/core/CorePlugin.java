package whg.core;

import net.whg.we.main.Plugin;
import net.whg.we.rendering.Graphics;
import net.whg.we.resources.GLSLShaderLoader;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.window.QueuedWindow;

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

	@Override
	public String getPluginName()
	{
		return "Core";
	}

	@Override
	public void initPlugin()
	{
		// Build event threads
		_graphicsThread = new GraphicsThread(this);
		_physicsThread = new PhysicsThread();

		_graphicsThread.build();

		// Build resource loaders
		ResourceLoader.addFileLoader(new GLSLShaderLoader());
	}

	@Override
	public void enablePlugin()
	{
		_graphicsThread.start();
		_physicsThread.start();
	}

	@Override
	public int getPriority()
	{
		return 10000;
	}

	public QueuedWindow getWindow()
	{
		return _graphicsThread.getWindow();
	}

	public Graphics getGraphics()
	{
		return _graphicsThread.getGraphics();
	}
}
