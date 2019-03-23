package net.whg.we.client_logic.resources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;
import net.whg.we.utils.logging.Log;

/**
 * Represents a Yaml based resource which can read and write data from
 * ResourceFiles.
 *
 * @author TheDudeFromCI
 */
public class YamlFile
{
	private Map<String, Object> _map;

	/**
	 * Clears the currently held data structure, and loads a new data struction from
	 * the requested resource file. The file will attempt to be parsed reguardless
	 * of the file type.
	 *
	 * @param resource - The Yaml file to load.
	 * @return True if the file was successfully loaded. False otherwise.
	 */
	public boolean load(File file)
	{
		Log.infof("Loading YAML file %s.", file);

		Yaml yaml = new Yaml();
		try
		{
			_map = yaml.load(new FileReader(file));
			return true;
		}
		catch (FileNotFoundException e)
		{
			Log.errorf("Failed to load yaml file %s!", e, file);
			return false;
		}
	}

	/**
	 * Gets the root nodes of the data tree as a map. The map keys represent root
	 * paths, and the values are an object representing either another tree
	 * recursively, or a straight value for that node.
	 *
	 * @return A map of the root nodes of this data tree.
	 */
	public Map<String, Object> getRoots()
	{
		return _map;
	}

	/**
	 * Gets a list of all child paths of the node at the requested path.
	 *
	 * @param path
	 *            - The part of the parent node, seperated by periods.
	 * @return A set of the names of all child nodes of the node at the requested
	 *         path.
	 */
	public Set<String> getKeys(String path)
	{
		return getKeys(path.split("\\."));
	}

	/**
	 * Gets a list of all child paths of the node at the requested path.
	 *
	 * @param path
	 *            - The part of the parent node, where each parameter represents a
	 *            child of the previous parameter.
	 * @return A set of the names of all child nodes of the node at the requested
	 *         path.
	 */
	@SuppressWarnings("unchecked")
	public Set<String> getKeys(String... path)
	{
		Map<String, Object> m = _map;

		for (String s : path)
			m = (Map<String, Object>) m.get(s);

		return m.keySet();
	}

	/**
	 * Gets the value at the requested location, or null if the path does not exist,
	 * or is not a value.
	 *
	 * @param path
	 *            - The path to retrieve the value from, seperated by periods.
	 * @return The object value at the specified location, or null if no value is
	 *         assigned.
	 */
	public Object getValue(String path)
	{
		return getValue(path.split("\\."));
	}

	/**
	 * Gets the value at the requested location, or null if the path does not exist,
	 * or is not a value.
	 *
	 * @param path
	 *            - The path to retrieve the value from, where each parameter
	 *            represents a child of the previous parameter.
	 * @return The object value at the specified location, or null if no value is
	 *         assigned.
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(String... path)
	{
		Map<String, Object> m = _map;

		for (int i = 0; i < path.length - 1; i++)
		{
			if (!m.containsKey(path[i]))
				return null;

			m = (Map<String, Object>) m.get(path[i]);
		}

		return m.get(path[path.length - 1]);
	}

	/**
	 * Gets the value at the specified location, casted as a string.
	 *
	 * @param path
	 *            - The path to retrieve the value from, seperated by periods.
	 * @return The value at the specified location as a String.
	 * @see {@link #getValue(String)}
	 */
	public String getString(String path)
	{
		return (String) getValue(path);
	}

	/**
	 * Gets the value at the specified location, casted as a string.
	 *
	 * @param path
	 *            - The path to retrieve the value from, where each parameter
	 *            represents a child of the previous parameter.
	 * @return The value at the specified location as a String.
	 * @see {@link #getValue(String...)}
	 */
	public String getString(String... path)
	{
		return (String) getValue(path);
	}

	/**
	 * Gets the value at the specified location, casted as an int.
	 *
	 * @param path
	 *            - The path to retrieve the value from, seperated by periods.
	 * @return The value at the specified location as an int.
	 * @see {@link #getValue(String)}
	 */
	public int getInt(String path)
	{
		return (int) getValue(path);
	}

	/**
	 * Gets the value at the specified location, casted as an int.
	 *
	 * @param path
	 *            - The path to retrieve the value from, where each parameter
	 *            represents a child of the previous parameter.
	 * @return The value at the specified location as an int.
	 * @see {@link #getValue(String...)}
	 */
	public int getInt(String... path)
	{
		return (int) getValue(path);
	}

	/**
	 * Gets the value at the specified location, casted as a boolean.
	 *
	 * @param path
	 *            - The path to retrieve the value from, seperated by periods.
	 * @return The value at the specified location as a boolean.
	 * @see {@link #getValue(String)}
	 */
	public boolean getBoolean(String path)
	{
		return (boolean) getValue(path);
	}

	/**
	 * Gets the value at the specified location, casted as a boolean.
	 *
	 * @param path
	 *            - The path to retrieve the value from, where each parameter
	 *            represents a child of the previous parameter.
	 * @return The value at the specified location as a boolean.
	 * @see {@link #getValue(String...)}
	 */
	public boolean getBoolean(String... path)
	{
		return (boolean) getValue(path);
	}
}
