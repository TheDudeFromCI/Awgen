package net.whg.we.resources;

/**
 * Represents a resource that can be reloaded at runtime. This is useful for
 * things such as streaming information from online, and downloading higher
 * resolution data in the background, or files that may change value
 * dynmanically, such as config files.
 *
 * @author TheDudeFromCI
 */
public interface ReloadableResource extends Resource
{
	/**
	 * Check if the resource needs to be reloaded. This should only be called from
	 * the resource owner's main thread.
	 *
	 * @return True if the resource needs to be reloaded. False otherwise.
	 */
	public boolean needsReloading();

	/**
	 * Reloads the resource. This changes the data object that is returned from the
	 * {@link #getData()} method. This should only be called if the
	 * {@link #needsReloading()} method returns true. This should only be called
	 * from the resource owner's main thread.
	 */
	public void reloadData();
}
