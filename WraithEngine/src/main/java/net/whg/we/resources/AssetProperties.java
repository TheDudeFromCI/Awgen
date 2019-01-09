package net.whg.we.resources;

import net.whg.we.main.Plugin;

public class AssetProperties
{
	private Plugin _plugin;
	private YamlFile _yaml;

	public AssetProperties(Plugin plugin, YamlFile yaml)
	{
		_plugin = plugin;
		_yaml = yaml;
	}

	public YamlFile getYaml()
	{
		return _yaml;
	}

	public Plugin getPlugin()
	{
		return _plugin;
	}
}
