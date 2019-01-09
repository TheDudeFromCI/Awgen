package net.whg.we.resources;

import java.util.Map;
import java.util.Set;
import net.whg.we.main.Plugin;

public class AssetProperties
{
	private Plugin _plugin;
	private Map<String, Object> _map;

	public AssetProperties(Plugin plugin, Map<String, Object> map)
	{
		_plugin = plugin;
		_map = map;
	}

	public Map<String, Object> getRoots()
	{
		return _map;
	}

	public Set<String> getKeys(String path)
	{
		return getKeys(path.split("\\."));
	}

	@SuppressWarnings("unchecked")
	public Set<String> getKeys(String... path)
	{
		Map<String, Object> m = _map;

		for (String s : path)
			m = (Map<String, Object>) m.get(s);

		return m.keySet();
	}

	public String getString(String path)
	{
		return getString(path.split("\\."));
	}

	@SuppressWarnings("unchecked")
	public String getString(String... path)
	{
		Map<String, Object> m = _map;

		for (int i = 0; i < path.length - 1; i++)
			m = (Map<String, Object>) m.get(path[i]);

		return (String) m.get(path[path.length - 1]);
	}

	public Plugin getPlugin()
	{
		return _plugin;
	}
}
