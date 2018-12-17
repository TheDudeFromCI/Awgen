package net.whg.we.main;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import net.whg.we.gamelogic.DefaultWindowListener;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;

/**
 * The program entry class. This class is used for the purpose of initializing
 * the plugin loader, all core plugins, and hunting down and launching local
 * plugins.
 *
 * @author TheDudeFromCI
 */
public class WraithEngine
{
	/**
	 * The current version of WraithEngine being run.
	 */
	public static final String VERSION = "v0.0.1";

	/**
	 * The program entry method.
	 *
	 * @param args
	 *            - Arguments from command line. Used for determining program
	 *            functions. Currently, there are no extra runtime flags that can be
	 *            used at this time.
	 */
	public static void main(String[] args)
	{
		// Set default log parameters
		Log.setLogLevel(Log.TRACE);

		// Log some automatic system info
		Log.trace("Starting WraithEngine.");
		Log.debugf("WraithEngine Version: %s", VERSION);
		Log.debugf("Operating System: %s", System.getProperty("os.name"));
		Log.debugf("Operating System Arch: %s", System.getProperty("os.arch"));
		Log.debugf("Java Version: %s", System.getProperty("java.version"));
		Log.debugf("Java Vendor: %s", System.getProperty("java.vendor"));
		Log.debugf("System User: %s", System.getProperty("user.name"));
		Log.debugf("Working Directory: %s", System.getProperty("user.dir"));
		Log.debugf("LWJGL Version: %s", Version.getVersion());

		// Load the launcher properties
		LauncherProperties properties = LauncherProperties.loadLauncherProperties();

		// Create plugin loader
		PluginLoader pluginLoader = new PluginLoader();

		// Load core plugins
		CorePlugin corePlugin = new CorePlugin();
		pluginLoader.loadPlugin(corePlugin);

		// Load external plugins
		corePlugin.loadPluginsFromFile(pluginLoader);

		// Enable plugins
		pluginLoader.enableAllPlugins();

		// Launch game window, if needed
		Log.trace("Checking if application is a console application.");
		if (!properties.isConsoleApplication())
		{
			Log.debug("Building game window.");

			Log.indent();
			QueuedWindow window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW)
					.setName("Untitled Project").setResizable(false).setSize(640, 480)
					.setVSync(false).setListener(new DefaultWindowListener()).build();
			Log.unindent();

			Log.trace("Starting game loop.");

			Log.trace("Setting default OpenGL clear color. (0.2f, 0.4f, 0.8f, 1f)");
			GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);

			while (true)
			{
				Time.updateTime();
				FPSLogger.logFramerate();

				float v = (float) Math.sin(Time.time() * 4f) * 0.5f + 0.5f;
				GL11.glClearColor(v, v, v, 1f);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				if (window.endFrame())
					break;
			}

			Log.debug("Disposing game.");
			// Nothing to dispose.

			Log.debug("Disposing OpenGL.");
			GL.setCapabilities(null);
		}

		Log.trace("Closing WraithEngine.");
	}
}
