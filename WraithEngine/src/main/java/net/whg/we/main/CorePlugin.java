package net.whg.we.main;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.gamelogic.DefaultGameLoop;
import net.whg.we.gamelogic.DefaultWindowListener;
import net.whg.we.gamelogic.GameLoop;
import net.whg.we.utils.FileUtils;
import net.whg.we.utils.Log;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;

/**
 * This is the core plugin loader for WraithEngine. It is in charge of loading
 * and sending events between plugins.
 *
 * @author TheDudeFromCI
 */
public class CorePlugin extends BasePlugin
{
	@Override
	public String getPluginName()
	{
		return "Core";
	}

	void loadPluginsFromFile(PluginLoader pluginLoader)
	{
		File folder = FileUtils.getPluginFolder();

		for (File file : folder.listFiles())
		{
			String fileName = file.getName();

			if (file.isDirectory())
			{
				Log.warnf("File '%s' is in plugin folder, but is a directory!", fileName);
				continue;
			}

			if (!file.canRead())
			{
				Log.warnf("File '%s' is in plugin folder, but is not cannot be read!", fileName);
				continue;
			}

			if (!fileName.endsWith(".jar"))
			{
				Log.warnf("File '%s' is in plugin folder, but is not a jar file", fileName);
				continue;
			}

			Log.infof("Attempting to load plugin %s...", fileName);
			Plugin plugin = attemptLoadPlugin(file);

			if (plugin != null)
				pluginLoader.loadPlugin(plugin);
		}
	}

	private Plugin attemptLoadPlugin(File file)
	{
		try
		{
			URL[] url = new URL[]
			{
					file.toURI().toURL()
			};
			URLClassLoader classLoader = new URLClassLoader(url, this.getClass().getClassLoader());
			InputStream pluginProperties = classLoader.getResourceAsStream("plugin.yml");

			Yaml yaml = new Yaml();
			Map<String, Object> map = yaml.load(pluginProperties);

			String mainClassPath = (String) map.get("MainClass");

			Class<?> mainClass = Class.forName(mainClassPath, true, classLoader);
			Plugin plugin = (Plugin) mainClass.newInstance();

			return plugin;
		}
		catch (MalformedURLException exception)
		{
			Log.errorf("Failed to read file for plugin %s!", exception, file.getName());
			return null;
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| ClassCastException exception)
		{
			Log.errorf("Failed to load main class for plugin %s!", exception, file.getName());
			return null;
		}
	}

	void start()
	{
		LauncherProperties properties = LauncherProperties.loadLauncherProperties();

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

			GameLoop loop = new DefaultGameLoop(window);
			loop.loop();
		}
	}
}
