package net.whg.we.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.utils.Log;

public class YamlFile
{
	private Map<String, Object> _map;

	public boolean load(File file)
	{
		Yaml yaml = new Yaml();
		try
		{
			_map = yaml.load(new FileReader(file));
			return true;
		}
		catch (FileNotFoundException e)
		{
			Log.errorf("Failed to load yaml file! %s", e, file.toString());
			return false;
		}
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
}
