package net.whg.we.event;

import java.util.ArrayList;
import java.util.LinkedList;
import net.whg.we.main.Plugin;
import net.whg.we.utils.Log;

/**
 * A default setup for an event caller, which handles most common functions for
 * you. This class is thread safe and handles error catching. This default setup
 * only requires the {@link #runEvent(Listener, int)} method to be defined for
 * local event callers. For registered event callers, it is recommended to also
 * define the {@link #getName()} and {@link #getPlugin()} methods as well.
 *
 * @param <T>
 *            - The type of listener this event calls to.
 */
public abstract class EventCallerBase<T extends Listener> implements EventCaller<T>
{
	protected ArrayList<T> _listeners = new ArrayList<>();
	private LinkedList<T> _pendingAdd = new LinkedList<>();
	private LinkedList<T> _pendingRemove = new LinkedList<>();
	private boolean _isInEvent;
	private Object _lock = new Object();
	private boolean _disposeOnFinish;
	private String _defaultName;

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

		synchronized (_lock)
		{
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
	}

	@Override
	public void removeListener(T listener)
	{
		Log.infof("Removed a listener %s, from the event caller %s from the plugin %s.",
				listener.getClass().getName(), getName(), getPlugin().getPluginName());

		synchronized (_lock)
		{
			if (_isInEvent)
			{
				_pendingRemove.add(listener);
				return;
			}

			_listeners.remove(listener);
		}
	}

	@Override
	public void dispose()
	{
		Log.infof("Disposed the event caller %s from the plugin %s", getName(),
				getPlugin().getPluginName());

		synchronized (_lock)
		{
			if (_isInEvent)
			{
				_disposeOnFinish = true;
				return;
			}

			_listeners.clear();
			_pendingAdd.clear();
			_pendingRemove.clear();
		}
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

	protected void callEvent(int index, Object... args)
	{
		synchronized (_lock)
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
	}

	protected abstract void runEvent(T t, int index, Object[] args);
}
