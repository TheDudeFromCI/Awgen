package net.whg.frameworks.resource;

import java.util.UUID;

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

	/**
	 * Gets the UUID of this resource object. This value is persistent for the
	 * lifetime of this resource object across shutdowns. This value can be used to
	 * identify a resource object in the instance that it is moved from its original
	 * location.
	 *
	 * @return The UUID of this resource object.
	 */
	UUID getUUID();
}
