package net.whg.frameworks.resource;

/**
 * A resource future is a loading state of a resource. This interface is used to
 * safely communicate with a resource loading thread and copy information over
 * from it periodically. This is the class instance that actually loads a
 * resource into the resource data class.
 *
 * @author TheDudeFromCI
 */
public interface ResourceFuture extends Runnable
{
	public static final int NO_CHANGE = 0;
	public static final int PARTIALLY_LOADED = 1;
	public static final int FULLY_LOADED = 2;
	public static final int UNABLE_TO_LOAD = 3;

	/**
	 * This method is called from the main thread to attempt to syncrhonize the
	 * working from this resource loader to the output resource data state. The
	 * resource data given may be disposed, in which case, this resource future may
	 * override that state.
	 *
	 * @param data
	 *            - The resource data to write information
	 * @return The current load operation state that was applied to this the given
	 *         resource data.<br>
	 * 		One of:
	 *         <ul>
	 *         <li>NO_CHANGE</li>
	 *         <li>PARTIALLY_LOADED</li>
	 *         <li>FULLY_LOADED</li>
	 *         <li>UNABLE_TO_LOAD</li>
	 *         </ul>
	 */
	int sync(ResourceData data);
}
