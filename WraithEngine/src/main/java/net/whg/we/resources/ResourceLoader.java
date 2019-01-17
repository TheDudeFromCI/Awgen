package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.utils.Log;
import java.util.HashMap;

public class ResourceLoader
{
	private ArrayList<FileLoader<?>> _fileLoaders = new ArrayList<>();
	private ArrayList<FileLoader<?>> _fileLoaderBuffer = new ArrayList<>();
	private static HashMap<ResourceFile, Resource<?>> _resourceReferences = new HashMap<>();
	private FileDatabase _fileDatabase;

	public ResourceLoader(FileDatabase fileDatabase)
	{
		_fileDatabase = fileDatabase;
	}

	/**
	 * Gets the file database currently being used by this ResourceLoader.
	 *
	 * @return The file database.
	 */
	public FileDatabase getFileDatabase()
	{
		return _fileDatabase;
	}

	/**
	 * Loads a file as a resource.<br>
	 * This method works by scanning all referenced file loaders and returning the
	 * file loader with the highest priority that supports the given file type. If
	 * this file does not have an extension, null is returned. If no file loader
	 * supports this file extension, null is returned. If the file fails to load,
	 * null is returned.
	 *
	 * @param file
	 *            - The file to load.
	 * @return A loaded resource for the file, or null if the file cannot be loaded.
	 */
	public Resource<?> loadResource(ResourceFile resource)
	{
		// Check to see if the resource is already loaded
		if (hasResource(resource))
			return getResource(resource);

		Log.infof("Loading the resource %s.", resource);

		FileLoader<?> loader = null;

		for (FileLoader<?> l : _fileLoaders)
			for (String s : l.getTargetFileTypes())
				if (s.equals(resource.getFileExtension()))
					_fileLoaderBuffer.add(l);

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

		Log.debugf("Loading resource %s using the file loader, %s.", resource.getName(),
				loader.getClass().getName());
		Resource<?> res = loader.loadFile(this, resource);

		if (res != null)
		{
			res.setResourceFile(resource);
			addResource(res);
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
	public void addFileLoader(FileLoader<?> fileLoader)
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
	public void removeFileLoader(FileLoader<?> fileLoader)
	{
		Log.debugf("Removing a file loader from the ResourceLoader, %s.",
				fileLoader.getClass().getName());

		_fileLoaders.remove(fileLoader);
	}

	/**
	 * Checks if this resource loader already have this resource loaded. If the resourceFile is not
	 * specified, this method returns false.
	 *
	 * @param resourceFile - The resource file that represents the given resource.
	 *
	 * @return True if the resource referenced by this resource file exists. False otherwise.
	 */
	public boolean hasResource(ResourceFile resourceFile)
	{
		if (resourceFile == null)
			return false;

		return _resourceReferences.containsKey(resourceFile);
	}

	/**
	 * Gets the loaded resource that is currently assigned to this ResourceFile.
	 *
	 * @param resourceFile - The resource file that represents the given resource.
	 *
	 * @return The loaded resource for the given ResourceFile, or null if the resource is not loaded,
	 * or if a ResourceFile is not specified.
	 */
	public Resource<?> getResource(ResourceFile resourceFile)
	{
		if (resourceFile == null)
			return null;

		return _resourceReferences.get(resourceFile);
	}

	/**
	 * Adds a loaded resource to this ResourceLoader. Loaded resources will be returned instead of
	 * attempting to load resources from file when possible.
	 *
	 * @param resource - The loaded resource to add to this ResourceLoader. If null, nothing will
	 * happen.
	 */
	public void addResource(Resource<?> resource)
	{
		if (resource == null)
		{
			Log.warn("Attempted to add a null resource to the ResourceLoader!");
			return;
		}

		_resourceReferences.put(resource.getResourceFile(), resource);
	}

	/**
	 * Removes a loaded resource from this ResourceLoader. This method will also attempt to dispose
	 * the given resource, even if the resource is not currently loaded into this ResourceLoader.
	 *
	 * @param resource - The loaded resource to remove to this ResourceLoader and dispose. If null,
	 * nothing will happen.
	 */
	public void removeResource(Resource<?> resource)
	{
		if (resource == null)
		{
			Log.warn("Attempted to remove a null resource from the ResourceLoader!");
			return;
		}

		_resourceReferences.remove(resource.getResourceFile());
		resource.dispose();
	}

	/**
	 * Disposes and removes all loaded resources that this ResourceLoader currently owns.
	 */
	public void disposeResources()
	{
		Log.info("Disposing all resources.");
		for (ResourceFile resourceFile : _resourceReferences.keySet())
		{
			Log.debugf("Disposing resources %s.", resourceFile);
			_resourceReferences.get(resourceFile).dispose();
		}

		_resourceReferences.clear();
	}
}
