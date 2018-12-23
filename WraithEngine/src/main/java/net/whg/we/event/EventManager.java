package net.whg.we.event;

import java.util.ArrayList;
import net.whg.we.main.Plugin;
import net.whg.we.utils.Log;

/**
 * The global event manager for adding all events to a singular location.
 *
 * @author TheDudeFromCI
 */
public class EventManager
{
	private static ArrayList<EventCaller<?>> _eventCallers = new ArrayList<>();

	/**
	 * Registers a new event caller to the database, allowing it to be referenced by
	 * other plugins. If the event caller is already in the database, nothing
	 * happens.
	 *
	 * @param eventCaller
	 *            - The event called to register.
	 */
	public static void registerEventCaller(EventCaller<?> eventCaller)
	{
		Log.infof("Registering event caller %s", eventCaller.getName());
		if (_eventCallers.contains(eventCaller))
		{
			Log.warnf("Event caller '%s' already registered, but tried to register again.",
					eventCaller.getName());
			return;
		}

		_eventCallers.add(eventCaller);
	}

	/**
	 * Removes an event called from the database, and disposes it. If the event
	 * called is not currently in the database, it is still disposed.
	 *
	 * @param eventCaller
	 *            - The event caller to unregister.
	 */
	public static void unregisterEventCaller(EventCaller<?> eventCaller)
	{
		Log.infof("Unregistering event caller %s", eventCaller.getName());

		if (Log.getLogLevel() <= Log.DEBUG)
		{
			if (!_eventCallers.contains(eventCaller))
			{
				Log.debug("Event not yet registered.");
				return;
			}
		}

		_eventCallers.remove(eventCaller);
	}

	/**
	 * Searchs the database for a specific event caller based on the name of the
	 * event caller and the plugin it is assigned to, and returns the first
	 * reference.
	 * 
	 * @param plugin
	 *            - The plugin that owns the event caller.
	 * @param name
	 *            - The name of the event caller.
	 * @return The event caller, or null if it is not found.
	 */
	public static EventCaller<?> getEventCaller(Plugin plugin, String name)
	{
		for (EventCaller<?> caller : _eventCallers)
			if (caller.getPlugin() == plugin && caller.getName().equals(name))
				return caller;
		return null;
	}

	/**
	 * Unregisters all event callers in the database.
	 *
	 * @see {@link EventManager#unregisterEventCaller(EventCaller)}
	 */
	public static void dispose()
	{
		Log.info("Disposing all event callers.");

		for (EventCaller<?> caller : _eventCallers)
			caller.dispose();

		_eventCallers.clear();
	}
}
