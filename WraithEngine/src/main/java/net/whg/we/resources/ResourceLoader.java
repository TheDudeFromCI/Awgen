package net.whg.we.resources;

import java.util.ArrayList;
import java.util.HashMap;
import net.whg.we.utils.Log;

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
	 * Loads a resource batch request. This method works by scanning all referenced
	 * file loaders and returning the file loader with the highest priority that
	 * supports the given file type for each file in the request. If the file does
	 * not have an extension, does not have a supported file loader, or fails to
	 * load, it is removed from the request. If addition resource dependencies are
	 * found while loading resources, they are automatically added to the request
	 * and loaded as well.
	 *
	 * @param request
	 *            - The resource files to load.
	 */
	public void loadResources(ResourceBatchRequest request)
	{
		if (Log.getLogLevel() <= Log.DEBUG)
		{
			Log.debugf("Loading ResourceBatchRequest.(%d Objects)", request.getResourceFileCount());
			for (int i = 0; i < request.getResourceFileCount(); i++)
				Log.debugf("  - %s", request.getResourceFile(i));
		}

		ResourceFile resourceFile;
		while ((resourceFile = request.nextUnloadedResource()) != null)
		{
			try
			{
				if (hasResource(resourceFile))
				{
					Log.debugf("Skipping resource %s, already loaded.", resourceFile);
					request.addResource(getResource(resourceFile));
					continue;
				}

				Log.infof("Loading the resource %s.", resourceFile);

				FileLoader<?> loader = null;
				for (FileLoader<?> l : _fileLoaders)
					for (String s : l.getTargetFileTypes())
						if (s.equals(resourceFile.getFileExtension()))
							_fileLoaderBuffer.add(l);

				if (_fileLoaderBuffer.isEmpty())
				{
					Log.warnf("Failed to load the resource %s, not a supported file type!",
							resourceFile);
					request.removeResourceFile(resourceFile);
					continue;
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
				Log.tracef("Using highest priority file loader: %s.", loader);

				FileLoadState fileLoadState = loader.loadFile(request, resourceFile);
				switch (fileLoadState)
				{
					case LOADED_SUCCESSFULLY:
						Log.tracef("Successfully loaded resource %s.", resourceFile);
						break;

					case FAILED_TO_LOAD:
						Log.warnf("Failed to load the resource %s, unable to parse!", resourceFile);
						request.removeResourceFile(resourceFile);
						break;

					case PUSH_TO_BACK:
						request.removeResourceFile(resourceFile);
						request.addResourceFile(resourceFile);
						break;

					default:
						Log.warn("Unknown File Load State!");
						break;
				}
			}
			catch (Exception exception)
			{
				Log.errorf("Uncaught error loading resource file %s!", exception, resourceFile);
			}
		}

		for (int i = 0; i < request.getResourceCount(); i++)
			addResource(request.getResource(i));
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
	 * Checks if this resource loader already have this resource loaded. If the
	 * resourceFile is not specified, this method returns false.
	 *
	 * @param resourceFile
	 *            - The resource file that represents the given resource.
	 * @return True if the resource referenced by this resource file exists. False
	 *         otherwise.
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
	 * @param resourceFile
	 *            - The resource file that represents the given resource.
	 * @return The loaded resource for the given ResourceFile, or null if the
	 *         resource is not loaded, or if a ResourceFile is not specified.
	 */
	public Resource<?> getResource(ResourceFile resourceFile)
	{
		if (resourceFile == null)
			return null;

		return _resourceReferences.get(resourceFile);
	}

	/**
	 * Adds a loaded resource to this ResourceLoader. Loaded resources will be
	 * returned instead of attempting to load resources from file when possible.
	 *
	 * @param resource
	 *            - The loaded resource to add to this ResourceLoader. If null,
	 *            nothing will happen.
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
	 * Removes a loaded resource from this ResourceLoader. This method will also
	 * attempt to dispose the given resource, even if the resource is not currently
	 * loaded into this ResourceLoader.
	 *
	 * @param resource
	 *            - The loaded resource to remove to this ResourceLoader and
	 *            dispose. If null, nothing will happen.
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
	 * Disposes and removes all loaded resources that this ResourceLoader currently
	 * owns.
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
