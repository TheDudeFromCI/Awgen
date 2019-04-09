package net.whg.frameworks.resource;

/**
 * Represents the current data state of a resource. All information about a
 * resource is stored in a resource data instance. This information be changed
 * at any time as a resource is loaded.
 *
 * @author TheDudeFromCI
 */
public interface ResourceData
{
	/**
	 * Disposes the information that is currently stored in this resource data
	 * instance.
	 */
	void dispose();
}
