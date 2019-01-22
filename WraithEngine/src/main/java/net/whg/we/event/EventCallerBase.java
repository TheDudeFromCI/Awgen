package net.whg.we.event;

import java.util.ArrayList;
import java.util.LinkedList;
import net.whg.we.main.Plugin;
import net.whg.we.utils.Log;

/**
 * A default setup for an event caller, which handles most common functions for you. This class
 * handles calling listeners, lists, and error catching. This default setup only requires the
 * {@link #runEvent(Listener, int)} method to be defined for local event callers. For registered
 * event callers, it is recommended to also define the {@link #getName()} and {@link #getPlugin()}
 * methods as well.
 *
 * @param <T> - The type of listener this event calls to.
 */
public abstract class EventCallerBase<T extends Listener> implements EventCaller<T>
{
	protected ArrayList<T> _listeners = new ArrayList<>();
	private LinkedList<T> _pendingAdd = new LinkedList<>();
	private LinkedList<T> _pendingRemove = new LinkedList<>();
	private boolean _isInEvent;
	private boolean _disposeOnFinish;
	private String _defaultName;

	/**
	 * Created a new EventCallerBase.
	 */
	public EventCallerBase()
	{
		_defaultName = getClass().getName();
	}

	@Override
	public void addListener(T listener)
	{
		if (getPlugin() == null)
			Log.infof("Adding a listener %s, to the event caller %s.", listener.getClass().getName(),
				getName());
		else
			Log.infof("Adding a listener %s, to the event caller %s from the plugin %s.",
					listener.getClass().getName(), getName(), getPlugin().getPluginName());

		if (_listeners.contains(listener))
			return;

		if (_isInEvent)
		{
			if (_pendingAdd.contains(listener))
				return;

			_pendingAdd.add(listener);
			return;
		}

		_listeners.add(listener);
		_listeners.sort((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()));
	}

	@Override
	public void removeListener(T listener)
	{
		if (getPlugin() == null)
			Log.infof("Removed a listener %s, from the event caller %s.",
					listener.getClass().getName(), getName());
		else
			Log.infof("Removed a listener %s, from the event caller %s from the plugin %s.",
					listener.getClass().getName(), getName(), getPlugin().getPluginName());

		if (_isInEvent)
		{
			_pendingRemove.add(listener);
			return;
		}

		_listeners.remove(listener);
	}

	@Override
	public void dispose()
	{
		if (getPlugin() == null)
			Log.infof("Disposed the event caller %s", getName());
		else
			Log.infof("Disposed the event caller %s from the plugin %s", getName(),
					getPlugin().getPluginName());

		if (_isInEvent)
		{
			_disposeOnFinish = true;
			return;
		}

		_listeners.clear();
		_pendingAdd.clear();
		_pendingRemove.clear();
	}

	@Override
	public String getName()
	{
		return _defaultName;
	}

	@Override
	public Plugin getPlugin()
	{
		return null;
	}

	/**
	 * Gets the number of listeners that are currently attached to this event caller.
	 *
	 * @@return The number of events currently attached to this event caller.
	 */
	public int getListenerCount()
	{
		return _listeners.size();
	}

	/**
	 * Gets a specific listener based on it's index (or current call order) in the listener stack.
	 * Listeners can change indices whenever new listeners are added or removed,
	 *
	 * @return The listener at the specified index in the listener list.
	 */
	public T getListener(int index)
	{
		return _listeners.get(index);
	}

	/**
	 * Gets the current index of the specified listener. Listeners are called based on their index,
	 * where the index is equal to the number of other listeners that will be called before this one.
	 * A listener's index can change at any time as listeners are added or removed from this event
	 * caller.
	 *
	 * @return The index of this listener, or -1 if it is not currently attached to this event
	 * caller.
	 */
	public int getListenerIndex(T listener)
	{
		return _listeners.indexOf(listener);
	}

	/**
	 * Calls an event based on an index. This method will simply call
	 * @{@link #runEvent(Listener, int, Object[])} for each listener currently attached to this
	 * event caller. This method handles common event caller issues such as error catching,
	 * concurrent modification, and disposal after event finished. It is recommended to use this
	 * wrapper method instead of calling listeners directly.
	 *
	 * @param index - The index of the evebt, This value is not used, and is mereley the event index
	 * passed to each call of @{@link #runEvent(Listener, int, Object[])}.
	 * @param args - The argument list of parameters to pass to the runEvent method.
	 */
	protected void callEvent(int index, Object... args)
	{
		_isInEvent = true;

		for (T t : _listeners)
		{
			try
			{
				runEvent(t, index, args);
			}
			catch (Exception exception)
			{
				Log.errorf("Uncaught exception thrown from event listener!", exception);
			}
		}

		_isInEvent = false;

		if (_disposeOnFinish)
		{
			dispose();
			return;
		}

		for (T t : _pendingAdd)
			_listeners.add(t);

		for (T t : _pendingRemove)
			_listeners.remove(t);

		if (!_pendingAdd.isEmpty())
			_listeners.sort((o1, o2) -> Integer.compare(o1.getPriority(), o2.getPriority()));

		_pendingAdd.clear();
		_pendingRemove.clear();
	}

	/**
	 * This method is called by @{@link #callEvent(int, Object...)} for each listener attached to
	 * this event caller. This method should be used for the purpose of physically calling events
	 * on the given listener based on the event index.
	 *
	 * @param listener - The listener to call the event method for.
	 * @param index - The event index, provided by @{@link #callEvent(int, Object...)}, as to
	 * indicate which event should be called.
	 * @param args - The argument parameters to pass along to the listener.
	 */
	protected abstract void runEvent(T listener, int index, Object[] args);
}
