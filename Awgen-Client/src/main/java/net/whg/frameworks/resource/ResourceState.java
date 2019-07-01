package net.whg.frameworks.resource;

public enum ResourceState
{
	/**
	 * This state refers that a resource is still being loaded. The data for this
	 * resource may return null or an empty value until the resource is finished
	 * loading.
	 */
	UNLOADED(false, true),

	/**
	 * This state refers that a resource has been loaded partially. During this
	 * state, a low quality version of the resource has been loaded, but the high
	 * quality version of this resource is still being loaded. During this state,
	 * the resource can safely be used, but will require the resource to be loaded
	 * at a later time.
	 */
	PARTIALLY_LOADED(true, true),

	/**
	 * This state refers that the resource has been fully loaded and can be used
	 * fully in any way.
	 */
	FULLY_LOADED(true, false),

	/**
	 * When this state is returned, the resource has failed to finished loading for
	 * any reason. The resource is marked as broken.
	 */
	UNABLE_TO_LOAD(false, false),

	/**
	 * When this state is returned, a newer version of this resource exists, and is
	 * currently loading in the background. The resource can still be used during
	 * this state, but will require loading at a later time.
	 */
	OUTDATED(true, true),

	/**
	 * This state is returned after a resource is fully disposed and can no longer
	 * be used.
	 */
	DISPOSED(false, false);

	private boolean _canBeUsed;
	private boolean _isLoading;

	private ResourceState(boolean canBeUsed, boolean isLoading)
	{
		_canBeUsed = canBeUsed;
		_isLoading = isLoading;
	}

	/**
	 * Checks whether the resource is currently in a usable state or not.
	 *
	 * @return True if this resource state currently contains usable data, false
	 *         otherwise.
	 */
	public boolean canBeUsed()
	{
		return _canBeUsed;
	}

	/**
	 * Checks whether this resource state is still loading some data in the
	 * background.
	 *
	 * @return True if this resource is still loading data in the background. False
	 *         otherwise.
	 */
	public boolean isLoading()
	{
		return _isLoading;
	}
}
