package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.utils.Poolable;

/**
 * Represents a list of resources to be loaded. Files may be added to this batch file during the
 * loading process as needed based on resource dependencies. This object assumes that all loaded
 * Resources are returned back to this batch request after they are loaded.
 */
public class ResourceBatchRequest implements Poolable
{
	private ArrayList<ResourceFile> _resourceFiles = new ArrayList<>();
	private ArrayList<Resource> _resources = new ArrayList<>();

	/**
	 * Adds a resource file to this batch request to be loaded. If this resource file already exists
	 * within the batch request, nothing happens. If the resourceFile is null, nothing happens.
	 *
	 * @param resourceFile - The ResourceFile to add to this batch request.
	 */
	public void addResourceFile(ResourceFile resourceFile)
	{
		if (resourceFile == null)
			return;

		if (!_resourceFiles.contains(resourceFile))
			_resourceFiles.add(resourceFile);
	}

	/**
	 * Removes a ResourceFile from from this batch request. If this ResourceFile is not in this
	 * batch request, nothing happens. This method does not remove any resources that were loaded
	 * from this ResourceFile. If the resourceFile is null, nothing happens.
	 *
	 * @param resourceFile - The ResourceFile to remove from this batch request.
	 */
	public void removeResourceFile(ResourceFile resourceFile)
	{
		if (resourceFile == null)
			return;

		_resourceFiles.remove(resourceFile);
	}

	/**
	 * Added a loaded resource to this batch request. This indicates that the resource has been
	 * loaded as a result of this batch request. If the resource is null, nothing happens.
	 *
	 * @param resource - The loaded resource to add to this batch request.
	 */
	public void addResource(Resource resource)
	{
		if (resource == null)
			return;

		if (!_resources.contains(resource))
			_resources.add(resource);
	}

	/**
	 * Gets the number of ResourceFiles that are currently in this batch request.
	 *
	 * @return The number of ResourceFiles in this batch request.
	 */
	public int getResourceFileCount()
	{
		return _resourceFiles.size();
	}

	/**
	 * Gets the number of loaded resources that are currently in this batch request.
	 *
	 * @return The number of loaded resources in this batch request.
	 */
	public int getResourceCount()
	{
		return _resources.size();
	}

	/**
	 * Gets the ResourceFile at the specified index.
	 *
	 * @return The ResourceFile at the specified index.
	 */
	public ResourceFile getResourceFile(int index)
	{
		return _resourceFiles.get(index);
	}

	/**
	 * Gets the Resource at the specified index.
	 *
	 * @return The Resource at the specified index.
	 */
	public Resource getResource(int index)
	{
		return _resources.get(index);
	}

	/**
	 * Checks if all currently listed ResourceFiles in this batch request have loaded resources
	 * attached to this batch request. The method checks if @{link #nextUnloadedResource()} returns
	 * null.
	 *
	 * @return True is all ResourceFiles in this batch request have an associated Resource listed
	 * within this batch request. False otherwise.
	 *
	 * @see @{link #nextUnloadedResource()}
	 */
	public boolean isFullyLoaded()
	{
		return nextUnloadedResource() == null;
	}

	/**
	 * Returns the first ResourceFile found wthin this batch request that does not have an
	 * associated Resource file also within this batch request.
	 *
	 * @return The first unloaded ResourceFile in this batch request, or null if all ResourceFiles
	 * have been loaded.
	 */
	public ResourceFile nextUnloadedResource()
	{
		fileChecker:
		for (ResourceFile resourceFile : _resourceFiles)
		{
			for (Resource resource : _resources)
				if (resource.getResourceFile().equals(resourceFile))
					continue fileChecker;

			return resourceFile;
		}

		return null;
	}

	/**
	 * Clears all ResourceFiles and Resources from this batch request. This does not dispose any
	 * resources, and simply only clears the references this object has to them. This allows This
	 * class to safely be recycled.
	 */
	public void clear()
	{
		_resourceFiles.clear();
		_resources.clear();
	}

	@Override
	public void init()
	{
		// Nothing to do
	}

	@Override
	public void dispose()
	{
		clear();
	}
}
