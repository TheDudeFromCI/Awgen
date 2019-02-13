package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.rendering.Graphics;
import net.whg.we.utils.GenericRunnable;

/**
 * Represents a collection of loaded resources.
 *
 * @author TheDudeFromCI
 */
public class ResourceDatabase
{
	private ArrayList<Resource> _resources = new ArrayList<>();

	/**
	 * Adds a loaded resource to this database. If this resource is already in the
	 * database, nothing happens. If null is given as a parameter, nothing happens.
	 *
	 * @param resource
	 *            - The resource to add.
	 */
	public void addResource(Resource resource)
	{
		if (resource == null)
			return;

		if (!_resources.contains(resource))
			_resources.add(resource);
	}

	/**
	 * Removes a loaded resource from this database and disposes it. If this
	 * resource is not in the database, dispose is still called. If null is given as
	 * a parameter, nothing happens.
	 *
	 * @param resource
	 *            - The resource to dispose and remove.
	 */
	public void removeResource(Resource resource)
	{
		if (resource == null)
			return;

		resource.dispose();
		_resources.remove(resource);
	}

	/**
	 * Disposes all resources from this database and removes them. This database can
	 * safely be used afterwards.
	 */
	public void dispose()
	{
		for (Resource res : _resources)
			res.dispose();
		_resources.clear();
	}

	/**
	 * Gets a resource based on the resource file it is attached to. If the input
	 * resource file is null, null is returned.
	 *
	 * @param resourceFile
	 *            - The resource file to search with.
	 * @return The first resource which returns a resource file equal to the given
	 *         resource file parameter. If no resource is found, null is returned.
	 */
	public Resource getResource(ResourceFile resourceFile)
	{
		if (resourceFile == null)
			return null;

		for (Resource res : _resources)
			if (res.getResourceFile() != null && res.getResourceFile().equals(resourceFile))
				return res;
		return null;
	}

	/**
	 * Gets the number of loaded resources currently in this database.
	 *
	 * @return The number of loaded resources in this database.
	 */
	public int getResourceCount()
	{
		return _resources.size();
	}

	/**
	 * Gets a resource in this database based on it's index. A resource's index may
	 * change any time a resource is added to or removed from this list. This method
	 * is intended for iteration purposes only. This value should be between 0 and
	 * {@link #getResourceCount()}, inclusive.
	 *
	 * @param index
	 *            - The index of the resource to get.
	 * @return The resource at the provided index.
	 */
	public Resource getResourceAt(int index)
	{
		return _resources.get(index);
	}

	/**
	 * Runs an active for each resource in the database.
	 *
	 * @param action
	 *            - The action to preform on the resource.
	 */
	public void forEach(GenericRunnable<Resource> action)
	{
		for (Resource res : _resources)
			action.run(res);
	}

	/**
	 * Attempts to compile all resources if they are not already compiled.
	 *
	 * @param graphics
	 *            - The graphics manager to compile the resources with.
	 */
	public void compileAllResources(Graphics graphics)
	{
		for (Resource res : _resources)
		{
			if (res instanceof CompilableResource)
			{
				CompilableResource c = (CompilableResource) res;
				if (!c.isCompiled())
					c.compile(graphics);
			}
		}
	}
}
