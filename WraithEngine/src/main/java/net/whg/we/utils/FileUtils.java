package net.whg.we.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import net.whg.we.main.Plugin;

/**
 * A collection of various utility functions for dealing with file and folder
 * management.
 *
 * @author TheDudeFromCI
 */
public class FileUtils
{
	/**
	 * The name of the folder which contains working plugins.
	 */
	public static final String PLUGIN_FOLDER_NAME = "plugins";

	/**
	 * The name of the folder which contains working resources.
	 */
	public static final String RESOURCE_FOLDER_NAME = "res";

	/**
	 * Gets the working directory for the current application.
	 *
	 * @return The current working directory.
	 */
	public static File getCoreFolder()
	{
		String dir = System.getProperty("user.dir");
		return new File(dir);
	}

	/**
	 * Gets the current plugin folder for accessing all plugins. If the folder does
	 * not exist yet, it is created.
	 *
	 * @return The plugin folder.
	 */
	public static File getPluginFolder()
	{
		File file = new File(getCoreFolder(), PLUGIN_FOLDER_NAME);

		if (!file.exists())
			file.mkdirs();

		return file;
	}

	/**
	 * Gets the current resource folder for the specified plugin.
	 *
	 * @param plugin
	 *            - The plugin to access the resources for.
	 * @return The resource folder for a certain plugin, or null if no plugin is
	 *         defined.
	 */
	public static File getResourcesFolder(Plugin plugin)
	{
		if (plugin == null)
			return null;

		File file = new File(getCoreFolder(), RESOURCE_FOLDER_NAME);
		file = new File(file, plugin.getPluginName());

		Log.tracef("Loading resource folder for plugin %s at %s.", plugin.getPluginName(), file);

		if (!file.exists())
			file.mkdirs();

		return file;
	}

	/**
	 * Returns a specific resource by name. The name of a resource is defined as the
	 * path to the resource and name of the resource relative to the plugin resource
	 * folder. Folder subdirectories are seperated by the character '/'. <br>
	 * <br>
	 * Example:
	 *
	 * <pre>
	 * path / to / resource.png
	 * </pre>
	 *
	 * @param plugin
	 *            - The plugin to get the resource from.
	 * @param name
	 *            - The path to a resource relative to the plugin resource folder.
	 * @return The resource at the specified location, or null if no resource is
	 *         found.
	 * @see {@link #getResourcesFolder}
	 */
	public static File getResource(Plugin plugin, String name)
	{
		if (plugin == null)
		{
			Log.warnf("Plugin not defined! Cannot retrieve resource %s!", name);
			return null;
		}

		Log.debugf("Attempting to load resource %s/%s", plugin.getPluginName(), name);

		name = name.replace('/', File.separatorChar);
		File file = new File(getResourcesFolder(plugin), name);

		Log.tracef("  Full Path: %s", file);

		if (!file.exists())
		{
			Log.warnf("Failed to load resource %s/%s, file not found!", plugin.getPluginName(),
					name);
			return null;
		}

		return file;
	}

	/**
	 * Loads a file and reads all contents into a string. Throws exceptions if file
	 * cannot be read.
	 *
	 * @param file
	 *            - The file to read string data from.
	 * @return The file's contents in a single string.
	 * @throws FileNotFoundException
	 *             If the file does not exist.
	 * @throws IOException
	 *             If the file cannot be read.
	 */
	public static String loadFileAsString(File file) throws FileNotFoundException, IOException
	{
		StringBuilder sb = new StringBuilder();

		try (BufferedReader in = new BufferedReader(new FileReader(file)))
		{
			String line;
			while ((line = in.readLine()) != null)
			{
				sb.append(line);
				sb.append('\n');
			}
		}

		return sb.toString();
	}

	/**
	 * Gets the file type (file extension) of the specified file. If no file is
	 * specified, null is returned.
	 *
	 * @param file
	 *            - The file to get the type of.
	 * @return A string representing the file extension of this file, or null if
	 *         this file does not have a file extention, or points to a folder.
	 */
	public static String getFileType(File file)
	{
		if (file == null)
		{
			Log.warn("Cannot get extention of null file!");
			return null;
		}

		if (file.isDirectory())
			return null;

		String fileName = file.getName();
		int lastDot = fileName.lastIndexOf(".");

		if (lastDot == -1)
			return null;

		return fileName.substring(lastDot + 1);
	}
}
