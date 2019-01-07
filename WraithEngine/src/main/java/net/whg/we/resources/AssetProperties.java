package net.whg.we.resources;

import java.util.Map;

public class AssetProperties
{
	private Map<String, Object> _map;

	public AssetProperties(Map<String, Object> map)
	{
		_map = map;
	}

	public String getString(String key)
	{
		return getString(key, null);
	}

	public String getString(String key, String def)
	{
		return (String) _map.getOrDefault(key, def);
	}
}
