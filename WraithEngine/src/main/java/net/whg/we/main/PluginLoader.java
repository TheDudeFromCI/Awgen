package net.whg.we.main;

import java.util.ArrayList;
import java.util.Comparator;
import net.whg.we.utils.Log;

class PluginLoader
{
	private ArrayList<Plugin> _plugins = new ArrayList<>();
	private Comparator<Plugin> _pluginSorter;

	PluginLoader()
	{
		_pluginSorter = (a, b) -> Integer.compare(a.getPriority(), b.getPriority());
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
		Log.tracef("Checking if %s is initialized.", plugin.getPluginName());
		if (!plugin.isInitialized())
		{
			Log.debugf("Initializing %s.", plugin.getPluginName());
			plugin.initPlugin();
		}

		Log.trace("Sorting plugins by priority.");
		Log.unindent();

		try
		{
			_plugins.sort(_pluginSorter);
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to sort plugin list!", exception);
		}
	}

	void enableAllPlugins()
	{
		Log.debug("Enabling plugins...");
		Log.indent();

		for (Plugin p : _plugins)
		{
			Log.debugf("Enabling %s.", p.getPluginName());
			p.enablePlugin();
		}

		Log.unindent();
		Log.debug("All plugins enabled.");
	}

	void disableAllPlugins()
	{
		Log.debug("Disabling plugins...");
		Log.indent();

		for (Plugin p : _plugins)
		{
			Log.debugf("Disabling %s.", p.getPluginName());
			p.disablePlugin();
		}

		Log.unindent();
		Log.debug("All plugins disabled.");
	}
}
