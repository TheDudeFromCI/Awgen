package net.whg.frameworks.resource;

import java.io.File;

/**
 * This class handles all high level API for resource management.
 *
 * @author TheDudeFromCI
 */
public class ResourceManager
{
	private ResourceDatabase _resourceDatabase;
	private ResourceLoader _resourceLoader;
	private FileDatabase _fileDatabase;

	/**
	 * Creates a new resource manager to manage the injected resource handlers.
	 *
	 * @param resourceDatabase
	 *            - The database which holds all currently loaded resources.
	 * @param resourceLoader
	 *            - The loader which handles loading new resources.
	 * @param fileDatabase
	 *            - The database which handles file processing.
	 */
	public ResourceManager(ResourceDatabase resourceDatabase, ResourceLoader resourceLoader,
			FileDatabase fileDatabase)
	{
		_resourceDatabase = resourceDatabase;
		_resourceLoader = resourceLoader;
		_fileDatabase = fileDatabase;
	}

	/**
	 * Gets the resource database this manager currently controls.
	 *
	 * @return The resource database.
	 */
	public ResourceDatabase getResourceDatabase()
	{
		return _resourceDatabase;
	}

	/**
	 * Gets the resource loader this manager currently controls.
	 *
	 * @return The resource loader.
	 */
	public ResourceLoader getResourceLoader()
	{
		return _resourceLoader;
	}

	/**
	 * Gets the file database this manager currently controls.
	 *
	 * @return The file database.
	 */
	public FileDatabase getFileDatabase()
	{
		return _fileDatabase;
	}

	/**
	 * Loads a resource from a resource file and returns the result. If the file is
	 * already loaded, then the loaded isntance of the resource is returned instead.
	 *
	 * @param resourceFile
	 *            - The resource file to load.
	 * @return The loaded resource, or null if the resource could not be loaded.
	 */
	public Resource loadResource(ResourceFile resourceFile)
	{
		return _resourceLoader.loadResource(resourceFile, this);
	}

	/**
	 * Disposes all resources currently loading within the database.
	 */
	public void disposeAllResources()
	{
		_resourceDatabase.dispose();
	}

	/**
	 * Gets the file that a resource file points to.
	 * 
	 * @param file
	 *            - The resource file that points to a certain file resource.
	 * @return The file that this resource file points to, or null if the file could
	 *         not be found.
	 */
	public File getFile(ResourceFile file)
	{
		return _fileDatabase.getFile(file);
	}
}
