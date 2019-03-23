package net.whg.we.client_logic.resources;

import java.util.ArrayList;
import net.whg.we.utils.logging.Log;

public class ResourceLoader
{
	private ArrayList<FileLoader> _fileLoaders = new ArrayList<>();

	/**
	 * Loads a resource from a file. If the resource already exists in the database,
	 * that instance of the resource is returned instead. The resource is added to
	 * the database after it is loaded. This method will also cause any resources
	 * that this resource depends on to also be loaded.
	 *
	 * @param resourceFile
	 *            - The resource file to load.
	 * @param database
	 *            - The database to load the resource from.
	 * @return
	 */
	public Resource loadResource(ResourceFile resourceFile, ResourceDatabase database)
	{
		Resource resource = database.getResource(resourceFile);
		if (resource != null)
			return resource;

		Log.infof("Loading the resource %s.", resourceFile);

		FileLoader loader = null;
		int priority = Integer.MIN_VALUE;

		file_loader:
		for (FileLoader l : _fileLoaders)
		{
			if (l.getPriority() <= priority)
				continue;
			for (String s : l.getTargetFileTypes())
				if (s.equals(resourceFile.getFileExtension()))
				{
					loader = l;
					continue file_loader;
				}
		}

		if (loader == null)
			throw new IllegalStateException(
					String.format("Not a supported file type! (%s)", resourceFile));

		return loader.loadFile(this, database, resourceFile);
	}

	/**
	 * Added a new file loader to the local references. If the file loader is
	 * already added, nothing happens. This method is thread safe.
	 *
	 * @param fileLoader
	 *            - The file loader to add.
	 */
	public void addFileLoader(FileLoader fileLoader)
	{
		Log.debugf("Adding a file loader to the ResourceLoader, %s.",
				fileLoader.getClass().getName());

		if (_fileLoaders.contains(fileLoader))
			return;
		_fileLoaders.add(fileLoader);
	}

	/**
	 * Removes a file loader from the local references. If the file load is not in
	 * the local references, nothing happens. This method is thread safe.
	 *
	 * @param fileLoader
	 *            - The file loader to remove.
	 */
	public void removeFileLoader(FileLoader fileLoader)
	{
		Log.debugf("Removing a file loader from the ResourceLoader, %s.",
				fileLoader.getClass().getName());

		_fileLoaders.remove(fileLoader);
	}

	/**
	 * Gets the number of file loaders currently attched to this resource loader.
	 *
	 * @return The number of file loaders currently attached to this resource
	 *         loader.
	 */
	public int getFileLoaderCount()
	{
		return _fileLoaders.size();
	}

	/**
	 * Gets the file loader at the specified index. A file loader's index can change
	 * anytime a new file loader is added or removed. This method is indented to be
	 * used for iteration purposes only.
	 *
	 * @param index
	 *            - The index of the file loader.
	 * @return The file loader at the specified index.
	 */
	public FileLoader getFileLoader(int index)
	{
		return _fileLoaders.get(index);
	}
}
