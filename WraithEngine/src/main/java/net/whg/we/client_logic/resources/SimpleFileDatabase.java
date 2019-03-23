package net.whg.we.client_logic.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.whg.we.main.Plugin;
import net.whg.we.utils.FileUtils;
import net.whg.we.utils.logging.Log;

public class SimpleFileDatabase implements FileDatabase
{
	public static final String PLUGIN_FOLDER_NAME = "plugins";
	public static final String RESOURCE_FOLDER_NAME = "res";

	private File _baseFolder;
	private SimplePathNameValidator _validator;

	public SimpleFileDatabase(File baseFolder)
	{
		_baseFolder = baseFolder;
		_validator = new SimplePathNameValidator();
	}

	@Override
	public List<File> getJarLibraries()
	{
		ArrayList<File> files = new ArrayList<>();

		File pluginFolder = new File(_baseFolder, PLUGIN_FOLDER_NAME);
		pluginFolder.mkdirs();

		for (File file : pluginFolder.listFiles())
		{
			if (!file.isFile())
				continue;

			if (!file.canRead())
				continue;

			if (file.isHidden())
				continue;

			if (FileUtils.getFileExtention(file.getName()).equals(".jar"))
				files.add(file);
		}

		return files;
	}

	@Override
	public ResourceFile getResourceFile(Plugin plugin, String pathName)
	{
		if (plugin == null)
		{
			Log.warnf("Plugin not defined! Cannot retrieve ResourceFile %s!", pathName);
			return null;
		}

		if (!_validator.isValidPathName(pathName))
		{
			Log.warnf("Path name could not be validated! Cannot retrieve ResourceFile %s!",
					pathName);
			return null;
		}

		File resourceFolder = new File(_baseFolder, RESOURCE_FOLDER_NAME);
		File pluginFolder = new File(resourceFolder, plugin.getPluginName());
		File file = new File(pluginFolder, FileUtils.pathnameToFile(pathName));

		return new ResourceFile(plugin, pathName, file);
	}

	public File getBaseFolder()
	{
		return _baseFolder;
	}

	@Override
	public PathNameValidator getPathNameValidator()
	{
		return _validator;
	}
}
