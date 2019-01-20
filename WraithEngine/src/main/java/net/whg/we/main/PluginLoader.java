package net.whg.we.main;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.utils.Log;
import net.whg.we.resources.FileDatabase;

public class PluginLoader
{
	private static ArrayList<Plugin> _plugins = new ArrayList<>();
	private static Comparator<Plugin> _pluginSorter;

	static
	{
		_pluginSorter = (a, b) -> Integer.compare(a.getPriority(), b.getPriority());
	}

	/**
	 * Attempts to load a plugin by the given name. Case sensitive.
	 *
	 * @param name
	 *            - The name of the plugin to load.
	 * @return The first plugin found with the given name or null if no plugin is
	 *         found.
	 */
	public static Plugin GetPlugin(String name)
	{
		for (Plugin p : _plugins)
			if (p.getPluginName().equals(name))
				return p;
		return null;
	}

	void loadPluginsFromFile(FileDatabase fileDatabase)
	{
		for (File file : fileDatabase.getJarLibraries())
		{
			String fileName = file.getName();
			Log.infof("Attempting to load plugin %s...", fileName);

			Plugin plugin = attemptLoadPlugin(file);

			if (plugin != null)
				loadPlugin(plugin);
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

	void loadPlugin(Plugin plugin)
	{
		if (_plugins.contains(plugin))
		{
			Log.tracef("Failed to add plugin %s to list, plugin already exists.",
					plugin.getPluginName());
			return;
		}
		_plugins.add(plugin);
		Log.debugf("Added plugin to list, %s", plugin.getPluginName());

		Log.indent();
		// Mark the current indention level.
		int indent = Log.getIndentLevel();

		try
		{
			Log.debugf("Initializing %s.", plugin.getPluginName());
			Log.indent();

			plugin.initPlugin();

			// Reset the indention to previous value, in case the plugin forgot to reset it
			Log.setIndentLevel(indent);
		}
		catch (Exception exception)
		{
			// As the plugin has failed to initialize, we can assume any ajustments to the
			// log indention level have never been corrected. Let's do that now.
			Log.setIndentLevel(indent);

			Log.errorf("Failed to initialize plugin %s!", exception, plugin.getPluginName());
			Log.warnf("Unloading uninitialized plugin, %s.", plugin.getPluginName());
			_plugins.remove(plugin);
		}

		Log.trace("Sorting plugins by priority.");

		try
		{
			_plugins.sort(_pluginSorter);
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to sort plugin list!", exception);
		}

		Log.unindent();
	}

	void enableAllPlugins()
	{
		Log.debug("Enabling plugins...");
		Log.indent();

		for (Plugin p : _plugins)
		{
			Log.debugf("Enabling %s.", p.getPluginName());

			int indent = Log.getIndentLevel();
			Log.indent();

			try
			{
				p.enablePlugin();
				Log.setIndentLevel(indent);
			}
			catch (Exception exception)
			{
				Log.setIndentLevel(indent);
				Log.errorf("Failed to enable plugin %s!", exception, p.getPluginName());
			}
		}

		Log.unindent();
		Log.debug("All plugins enabled.");
	}
}
