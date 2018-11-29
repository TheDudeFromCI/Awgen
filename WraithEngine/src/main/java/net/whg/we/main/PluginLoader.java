package net.whg.we.main;

import java.util.ArrayList;

import whg.WraithEngine.utils.Log;

class PluginLoader
{
	private ArrayList<Plugin> _plugins = new ArrayList<>();
	
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
	}
	
	public void initializeAllPlugins()
	{
		Log.debug("Initializing plugins...");
		for (Plugin p : _plugins)
		{
			Log.debugf("Initializing %s.", p.getPluginName());
			p.initPlugin();
		}
		Log.debug("All plugins initialized.");
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
