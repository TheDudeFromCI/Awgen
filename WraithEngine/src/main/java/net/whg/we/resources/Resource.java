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

	/**
	 * Gets the name of this resource. This should be unquie within the ResourceFile
	 * that is is loaded from.
	 *
	 * @return The name of this resource.
	 */
	public String getName();
}
