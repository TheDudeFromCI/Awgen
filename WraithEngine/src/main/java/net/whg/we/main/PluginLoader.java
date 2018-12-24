package net.whg.we.main;

import java.util.ArrayList;
import java.util.Comparator;
import net.whg.we.utils.Log;

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

	static void loadPlugin(Plugin plugin)
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
		Log.tracef("Checking if %s is initialized.", plugin.getPluginName());
		if (!plugin.isInitialized())
		{
			// Mark the current indention level.
			int indent = Log.getIndentLevel();

			try
			{
				Log.debugf("Initializing %s.", plugin.getPluginName());

				Log.indent();
				plugin.initPlugin();

				// Reset the indention to previous value, in case something went wrong while
				// initializing the plugin.
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

	static void enableAllPlugins()
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

	static void disableAllPlugins()
	{
		Log.debug("Disabling plugins...");
		Log.indent();

		for (Plugin p : _plugins)
		{
			Log.debugf("Disabling %s.", p.getPluginName());

			int indent = Log.getIndentLevel();
			Log.indent();

			try
			{
				p.disablePlugin();
				Log.setIndentLevel(indent);
			}
			catch (Exception exception)
			{
				Log.setIndentLevel(indent);
				Log.errorf("Failed to disable plugin %s!", exception, p.getPluginName());
			}
		}

		Log.unindent();
		Log.debug("All plugins disabled.");
	}
}
