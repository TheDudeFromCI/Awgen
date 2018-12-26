package net.whg.we.resources;

/**
 * Represents an asset resource that is loaded from file or downloaded. This is
 * a wrapper for the data, and may or may not represent the data itself.
 *
 * @author TheDudeFromCI
 */
public interface Resource
{
	/**
	 * Gets the raw data this resource represents. This should only be called from
	 * the resource owner's main thread.
	 *
	 * @return The data for this resource.
	 */
	public Object getData();
}
