package net.whg.we.resources;

import java.io.File;
import net.whg.we.main.Plugin;
import net.whg.we.utils.FileUtils;

/**
 * Represents a resource file on the disk.
 *
 * @author TheDudeFromCI
 */
public class ResourceFile
{
	private Plugin _plugin;
	private String _name;
	private String _assetExtension;
	private File _file;
	private File _propertiesFile;

	/**
	 * Creates a reference to a resource file on the disk.
	 *
	 * @param plugin
	 *            - The plugin that this resource belongs to.
	 * @param name
	 *            - The name of this asset relative to the plugins resource folder,
	 *            as defined by
	 *            {@link net.whg.we.utils.FileUtils#getResource(Plugin, String)}
	 */
	public ResourceFile(Plugin plugin, String name)
	{
		_plugin = plugin;
		_name = name;

		_file = FileUtils.getResource(plugin, name);
		_assetExtension = FileUtils.getFileType(_file);
		_propertiesFile = new File(_file.getAbsolutePath() + ".asset");
	}

	/**
	 * Gets the plugin that owns this resource.
	 *
	 * @return The plugin that owns this resource.
	 */
	public Plugin getPlugin()
	{
		return _plugin;
	}

	/**
	 * Checks if this resource currently exists or not.
	 *
	 * @return True if this resource exists, false otherwise.
	 */
	public boolean exists()
	{
		return _file != null && _file.exists();
	}

	/**
	 * Gets the name and relative path of this resource.
	 *
	 * @return The name of this resource file and path as defined by
	 *         {@link net.whg.we.utils.FileUtils#getResource(Plugin, String)}
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * Gets the file extension for this resource.
	 *
	 * @return The file extension for this resource.
	 */
	public String getFileExtension()
	{
		return _assetExtension;
	}

	/**
	 * Gets the raw file for this resource.
	 *
	 * @return The file for this resource, or null if this resource does not exist.
	 */
	public File getFile()
	{
		return _file;
	}

	/**
	 * Gets the properties file (.asset) for this resource.
	 *
	 * @return The properties file for this resource, or null if it does not exist.
	 */
	public File getPropertiesFile()
	{
		return _propertiesFile;
	}

	@Override
	public String toString()
	{
		return "[Resource: " + _plugin.getPluginName() + "/" + _name + "]";
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ResourceFile))
			return false;

		ResourceFile a = (ResourceFile) other;

		return _plugin == a._plugin && _name.equals(a._name);
	}

	@Override
	public int hashCode()
	{
		return _name.hashCode();
	}
}
