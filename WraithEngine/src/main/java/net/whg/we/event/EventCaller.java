package net.whg.we.event;

import net.whg.we.main.Plugin;

/**
 * An object which stores a list of listeners and sends events to them.
 *
 * @author TheDudeFromCI
 * @param <T>
 *            The type of listener that is accepted.
 */
public interface EventCaller<T extends Listener>
{
	/**
	 * Gets the name of this event caller. The name should ne unquie within the
	 * plugin which assigns it, and should never be changed. For better
	 * compatability, it is better to use a public string field for the name which
	 * can be referenced by other plugins through API.
	 *
	 * @return The name of this event caller.
	 */
	public String getName();

	/**
	 * Gets the plugin which owns this event caller. This should be the plugin which
	 * created the event caller, and should never change.
	 *
	 * @return The plugin that owns this event caller.
	 */
	public Plugin getPlugin();

	/**
	 * Adds a listener to the local database, and sorts the database based on the
	 * priority levels of each listener.
	 *
	 * @param listener
	 *            - The listener to add to the local database.
	 */
	public void addListener(T listener);

	/**
	 * Removes a listener from the local database, if it currently exists.
	 *
	 * @param listener
	 *            - The listener to remove from the database.
	 */
	public void removeListener(T listener);

	/**
	 * Remove all listeners from the local database, and dispose.
	 */
	public void dispose();
}
