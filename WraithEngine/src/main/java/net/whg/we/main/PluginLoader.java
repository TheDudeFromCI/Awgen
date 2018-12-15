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
		_pluginSorter = new Comparator<Plugin>()
		{
			@Override
			public int compare(Plugin a, Plugin b)
			{
				return Integer.compare(a.getPriority(), b.getPriority());
			}
		};
	}
	
	public void loadPlugin(Plugin plugin)
	{
		if (_plugins.contains(plugin))
		{
			Log.tracef("Failed to add plugin %s to list, plugin already exists.",
					plugin.getPluginName());
			return;
		}
		_plugins.add(plugin);
		Log.debugf("Added plugin to list, %s", plugin.getPluginName());
		
		Log.tracef("Checking if %s is initialized.", plugin.getPluginName());
		if (!plugin.isInitialized())
		{
			Log.debugf("Initializing %s.", plugin.getPluginName());
			plugin.initPlugin();
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
	}
	
	public void enableAllPlugins()
	{
		Log.debug("Enabling plugins...");
		for (Plugin p : _plugins)
		{
			Log.debugf("Enabling %s.", p.getPluginName());
			p.enablePlugin();
		}
		Log.debug("All plugins enabled.");
	}
	
	public void disableAllPlugins()
	{
		Log.debug("Disabling plugins...");
		for (Plugin p : _plugins)
		{
			Log.debugf("Disabling %s.", p.getPluginName());
			p.disablePlugin();
		}
		Log.debug("All plugins disabled.");
	}
}
