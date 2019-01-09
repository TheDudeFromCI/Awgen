package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.utils.Log;

public class ResourceLoader
{
	private static ArrayList<FileLoader<?>> _fileLoaders = new ArrayList<>();
	private static ArrayList<FileLoader<?>> _fileLoaderBuffer = new ArrayList<>();

	/**
	 * Loads a file as a resource.<br>
	 * This method works by scanning all referenced file loaders and returning the
	 * file loader with the highest priority that supports the given file type. If
	 * this file does not have an extension, null is returned. If no file loader
	 * supports this file extension, null is returned. If the file fails to load,
	 * null is returned. This method is thread safe.
	 *
	 * @param file
	 *            - The file to load.
	 * @return A loaded resource for the file, or null if the file cannot be loaded.
	 */
	public static Resource<?> loadResource(ResourceFile resource)
	{
		// Check to see if the resource is already loaded
		if (ResourceDatabase.hasResource(resource))
			return ResourceDatabase.getResource(resource);

		Log.infof("Loading the resource %s.", resource);

		FileLoader<?> loader = null;

		synchronized (_fileLoaderBuffer)
		{
			synchronized (_fileLoaders)
			{
				for (FileLoader<?> l : _fileLoaders)
					for (String s : l.getTargetFileTypes())
						if (s.equals(resource.getFileExtension()))
							_fileLoaderBuffer.add(l);
			}

			if (_fileLoaderBuffer.isEmpty())
			{
				Log.warnf("Failed to load the resource %s, not a supported file type!",
						resource.getName());
				return null;
			}

			if (Log.getLogLevel() <= Log.TRACE)
			{
				Log.trace("  Finding available file loaders...");
				for (FileLoader<?> l : _fileLoaderBuffer)
					Log.tracef("   * %s", l.getClass().getName());
			}

			if (_fileLoaderBuffer.size() == 1)
				loader = _fileLoaderBuffer.get(0);
			else
			{
				int pri = Integer.MIN_VALUE;
				for (int i = 0; i < _fileLoaderBuffer.size(); i++)
				{
					FileLoader<?> fl = _fileLoaderBuffer.get(i);
					if (fl.getPriority() > pri)
					{
						pri = fl.getPriority();
						loader = fl;
					}
				}
			}

			_fileLoaderBuffer.clear();
		}

		Log.debugf("Loading resource %s using the file loader, %s.", resource.getName(),
				loader.getClass().getName());
		Resource<?> res = loader.loadFile(resource);

		if (res != null)
		{
			res.setResourceFile(resource);
			ResourceDatabase.addResource(resource, res);
		}

		return res;
	}

	/**
	 * Added a new file loader to the local references. If the file loader is
	 * already added, nothing happens. This method is thread safe.
	 *
	 * @param fileLoader
	 *            - The file loader to add.
	 */
	public static void addFileLoader(FileLoader<?> fileLoader)
	{
		Log.debugf("Adding a file loader to the ResourceLoader, %s.",
				fileLoader.getClass().getName());

		synchronized (_fileLoaders)
		{
			if (_fileLoaders.contains(fileLoader))
				return;
			_fileLoaders.add(fileLoader);
		}
	}

	/**
	 * Removes a file loader from the local references. If the file load is not in
	 * the local references, nothing happens. This method is thread safe.
	 *
	 * @param fileLoader
	 *            - The file loader to remove.
	 */
	public static void removeFileLoader(FileLoader<?> fileLoader)
	{
		Log.debugf("Removing a file loader from the ResourceLoader, %s.",
				fileLoader.getClass().getName());

		synchronized (_fileLoaders)
		{
			_fileLoaders.remove(fileLoader);
		}
	}
}
