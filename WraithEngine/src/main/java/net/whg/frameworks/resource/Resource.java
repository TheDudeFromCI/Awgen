package net.whg.frameworks.resource;

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
	Object getData();

	/**
	 * Gets the resource file this resource represents. This can be used for
	 * identification purposes, or future file acessing.
	 *
	 * @return The resource file this resource represents.
	 */
	ResourceFile getResourceFile();

	/**
	 * Disposes this resource.
	 */
	void dispose();

	/**
	 * Gets the name of this resource. This should be unquie within the ResourceFile
	 * that is is loaded from.
	 *
	 * @return The name of this resource.
	 */
	String getName();

	/**
	 * Attempts to reload the data in this resource if a reload is required. If a
	 * reload is not currently required, nothing happens.
	 *
	 * @return True if the data this resource contains has been updated, false if
	 *         nothing has changed.
	 */
	boolean reload();

	/**
	 * Gets the current state of this resource.
	 * 
	 * @return The current state of this resource.
	 */
	ResourceState getResourceState();
}
