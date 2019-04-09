package net.whg.frameworks.resource;

/**
 * Represents an asset resource that is loaded from file or downloaded. This is
 * a wrapper for the data, and may or may not represent the data itself.
 *
 * @author TheDudeFromCI
 */
public class Resource
{
	private ResourceState _state;
	private ResourceFile _resourceFile;
	private ResourceManager _resourceManager;
	private ResourceFuture _future;
	private ResourceData _resourceData;

	public Resource(ResourceManager resourceManager, ResourceFile resourceFile,
			ResourceData resourceData)
	{
		_resourceManager = resourceManager;
		_resourceFile = resourceFile;
		_state = ResourceState.UNLOADED;
		_resourceData = resourceData;
	}

	/**
	 * Gets the resource file this resource represents. This can be used for
	 * identification purposes, or future file acessing.
	 *
	 * @return The resource file this resource represents.
	 */
	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	/**
	 * Disposes this resource. New information may be loaded to this resource if a
	 * new resource future is provided.
	 */
	public void dispose()
	{
		_state = ResourceState.DISPOSED;
		_resourceData.dispose();
		_future = null;
	}

	/**
	 * Attempts to reload the data in this resource if a reload is required. If a
	 * reload is not currently required, nothing happens.
	 *
	 * @return True if the data this resource contains has been updated, or false if
	 *         nothing has changed.
	 */
	public boolean reload()
	{
		if (!_state.isLoading())
			return false;

		if (_future == null)
			return false;

		int sync = _future.sync(_resourceData);

		if (sync == ResourceFuture.NO_CHANGE)
			return false;
		else if (sync == ResourceFuture.PARTIALLY_LOADED)
			_state = ResourceState.PARTIALLY_LOADED;
		else if (sync == ResourceFuture.FULLY_LOADED)
			_state = ResourceState.FULLY_LOADED;
		else if (sync == ResourceFuture.UNABLE_TO_LOAD)
			_state = ResourceState.UNABLE_TO_LOAD;

		return true;
	}

	/**
	 * Gets the resource manager that this resource is currently acting under.
	 *
	 * @return The resource manager which created this resource.
	 */
	public ResourceManager getResourceManager()
	{
		return _resourceManager;
	}

	/**
	 * Gets the current state of this resource.
	 *
	 * @return The current state of this resource.
	 */
	public ResourceState getResourceState()
	{
		return _state;
	}

	/**
	 * Gets the raw data this resource represents. This should only be called from
	 * the resource owner's main thread.
	 *
	 * @return The data for this resource, or null if this resource state does not
	 *         contain usable information for some reason.
	 */
	public ResourceData getData()
	{
		if (!_state.canBeUsed())
			return null;

		return _resourceData;
	}

	/**
	 * Sets the resource future for this resource.
	 *
	 * @param future
	 */
	public void setResourceFuture(ResourceFuture future)
	{
		if (future != null)
		{
			if (_state.canBeUsed())
				_state = ResourceState.OUTDATED;
			else
				_state = ResourceState.UNLOADED;
		}

		_future = future;
	}
}
