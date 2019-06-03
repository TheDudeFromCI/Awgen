package net.whg.we.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.whg.frameworks.resource.FileUtils;
import net.whg.frameworks.resource.ResourceFile;

public class SimpleFileDatabase implements ServerFileDatabase
{
	public static final String PLUGIN_FOLDER_NAME = "plugins";
	public static final String RESOURCE_FOLDER_NAME = "res";

	private File _baseFolder;

	public SimpleFileDatabase(File baseFolder)
	{
		_baseFolder = baseFolder;
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
	public File getFile(ResourceFile file)
	{
		return getFile(file.getPathname());
	}

	public File getRootFolder()
	{
		return _baseFolder;
	}

	public File getFile(String pathname)
	{
		File folder = new File(getRootFolder(), RESOURCE_FOLDER_NAME);
		return new File(folder, pathname);
	}
}
