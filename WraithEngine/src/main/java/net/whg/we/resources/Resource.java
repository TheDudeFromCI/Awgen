package net.whg.we.resources;

/**
 * Represents an asset resource that is loaded from file or downloaded. This is
 * a wrapper for the data, and may or may not represent the data itself.
 *
 * @author TheDudeFromCI
 */
public interface Resource<T>
{
	/**
	 * Gets the raw data this resource represents. This should only be called from
	 * the resource owner's main thread.
	 *
	 * @return The data for this resource.
	 */
	public T getData();

	/**
	 * Gets the resource file this resource represents. This can be used for
	 * identification purposes, or future file acessing.
	 *
	 * @return The resource file this resource represents.
	 */
	public ResourceFile getResourceFile();

	/**
	 * Disposes this resource.
	 */
	public void dispose();
}
