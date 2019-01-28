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
	private String _pathname;
	private String _assetExtension;
	private File _file;
	private File _propertiesFile;

	/**
	 * Creates a reference to a resource file on the disk.
	 *
	 * @param plugin
	 *            - The plugin that this resource belongs to.
	 * @param pathname
	 *            - The pathname of this asset relative to the plugins resource
	 *            folder.
	 * @param name
	 *            - The name of this resource within the file. If null, the filename
	 *            is used instead.
	 * @param file
	 *            - The file that the ResourceFile represents.
	 */
	public ResourceFile(Plugin plugin, String pathname, String name, File file)
	{
		_plugin = plugin;
		_name = name;
		_pathname = pathname;
		_file = file;
		_assetExtension = FileUtils.getFileExtention(pathname);
		_propertiesFile = new File(_file.getAbsolutePath() + ".asset");

		if (_name == null)
			_name = FileUtils.getSimpleFileName(_pathname);
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
	 * Checks if the properties file that this resource refers to exists.
	 *
	 * @return True if the properties file exists. False otherwise.
	 */
	public boolean hasPropertiesFile()
	{
		return _propertiesFile.exists();
	}

	/**
	 * Gets the name of this resource. If not specified, the name of the file is
	 * used instead.
	 *
	 * @return The name of this resource.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * Gets the file name and relative path of this resource.
	 *
	 * @return The name of this resource file and it's relative path.
	 */
	public String getPathName()
	{
		return _pathname;
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
	 * @return The file for this resource.
	 */
	public File getFile()
	{
		return _file;
	}

	/**
	 * Gets the properties file (.asset) for this resource.
	 *
	 * @return The properties file for this resource. May not exist.
	 */
	public File getPropertiesFile()
	{
		return _propertiesFile;
	}

	@Override
	public String toString()
	{
		return "[Resource: " + _plugin.getPluginName() + "/" + _pathname + ":" + _name + "]";
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ResourceFile))
			return false;

		ResourceFile a = (ResourceFile) other;

		return _plugin == a._plugin && getPathName().equals(a.getPathName())
				&& getName().equals(a.getName());
	}

	@Override
	public int hashCode()
	{
		return _pathname.hashCode();
	}
}
