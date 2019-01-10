package whg.core;

import net.whg.we.main.Plugin;
import net.whg.we.rendering.Graphics;
import net.whg.we.resources.GLSLShaderLoader;
import net.whg.we.resources.MeshSceneLoader;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.TextureLoader;
import net.whg.we.threads.ThreadManager;
import net.whg.we.window.QueuedWindow;

/**
 * This is the core plugin loader for WraithEngine. It is in charge of handling
 * the main game loops.
 *
 * @author TheDudeFromCI
 */
public class CorePlugin implements Plugin
{
	public static final String RENDERING_EVENTCALLER = "Core Rendering";
	public static final String PHYSICS_EVENTCALLER = "Core Physics";

	private ThreadManager _threadManager;

	@Override
	public String getPluginName()
	{
		return "Core";
	}

	@Override
	public void initPlugin()
	{
		// Build event threads
		_threadManager = new ThreadManager();
		_threadManager.buildThreads(this);

		// Build resource loaders
		ResourceLoader.addFileLoader(new GLSLShaderLoader());
		ResourceLoader.addFileLoader(new MeshSceneLoader());
		ResourceLoader.addFileLoader(new TextureLoader());
	}

	@Override
	public void enablePlugin()
	{
		_threadManager.startThreads();
	}

	public void requestClose()
	{
		getWindow().requestClose();
	}

	@Override
	public int getPriority()
	{
		return 10000;
	}

	public ThreadManager getThreadManager()
	{
		return _threadManager;
	}

	public QueuedWindow getWindow()
	{
		return _threadManager.getGraphicsThread().getWindow();
	}

	public Graphics getGraphics()
	{
		return _threadManager.getGraphicsThread().getGraphics();
	}
}
