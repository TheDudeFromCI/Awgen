package net.whg.we.resources;

public enum FileLoadState
{
	/**
	 * The file resource was correctly loaded.
	 */
	LOADED_SUCCESSFULLY,

	/**
	 * This file has failed to load for any reason.
	 */
	FAILED_TO_LOAD,

	/**
	 * This file requires depencencies to be loaded first, and should be pushed to
	 * the back of the resource loading queue.
	 */
	PUSH_TO_BACK
}
