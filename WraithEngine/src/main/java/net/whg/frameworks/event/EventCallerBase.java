package net.whg.frameworks.event;

import java.util.ArrayList;
import java.util.LinkedList;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.util.ObjectPool;
import net.whg.frameworks.util.SimpleObjectPool;
import net.whg.we.main.Plugin;

/**
 * A default setup for an event caller, which handles most common functions for
 * you. This class handles calling listeners, lists, and error catching. This
 * default setup only requires the {@link #runEvent(Listener, int, Object)}
 * method to be defined for local event callers. For registered event callers,
 * it is recommended to also define the {@link #getName()} and
 * {@link #getPlugin()} methods as well.
 *
 * @param <T>
 *            - The type of listener this event calls to.
 */
public abstract class EventCallerBase<T extends Listener> implements EventCaller<T>
{
	protected ArrayList<T> _listeners = new ArrayList<>();
	protected LinkedList<PendingEvent> _pendingEvents = new LinkedList<>();
	private LinkedList<T> _pendingAdd = new LinkedList<>();
	private LinkedList<T> _pendingRemove = new LinkedList<>();
	private boolean _isInEvent;
	private boolean _disposeOnFinish;
	private String _defaultName;
	private ObjectPool<PendingEvent> _eventPool = new SimpleObjectPool<>(PendingEvent.class);
	private Thread _mainThread;

	/**
	 * Creates a new EventCallerBase with the current thread being used as the
	 * execution thread.
	 */
	public EventCallerBase()
	{
		this(Thread.currentThread());
	}

	/**
	 * Creates a new EventCallerBase. The thread provided will be used as the
	 * execution thread for events. All events will be handled on that thread, and
	 * events called from other threads will be added to an internally queue to be
	 * processed on the main thread. If no thread is provided, all events be be
	 * handled instantly on the thread where the events are called from.
	 *
	 * @param mainThread
	 *            - The thread that all events will be handled on, or null to
	 *            disable thread synchronization.
	 */
	public EventCallerBase(Thread mainThread)
	{
		_defaultName = getClass().getName();
		_mainThread = mainThread;
	}

	@Override
	public void addListener(T listener)
	{
		if (listener == null)
			return;

		if (getPlugin() == null)
			Log.debugf("Adding a listener %s, to the event caller %s.",
					listener.getClass().getName(), getName());
		else
			Log.debugf("Adding a listener %s, to the event caller %s from the plugin %s.",
					listener.getClass().getName(), getName(), getPlugin().getPluginName());

		synchronized (_listeners)
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
		if (listener == null)
			return;

		if (getPlugin() == null)
			Log.debugf("Removed a listener %s, from the event caller %s.",
					listener.getClass().getName(), getName());
		else
			Log.debugf("Removed a listener %s, from the event caller %s from the plugin %s.",
					listener.getClass().getName(), getName(), getPlugin().getPluginName());

		synchronized (_listeners)
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
		if (getPlugin() == null)
			Log.debugf("Disposed the event caller %s", getName());
		else
			Log.debugf("Disposed the event caller %s from the plugin %s", getName(),
					getPlugin().getPluginName());

		synchronized (_listeners)
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

	/**
	 * Gets the number of listeners that are currently attached to this event
	 * caller.
	 *
	 * @@return The number of events currently attached to this event caller.
	 */
	public int getListenerCount()
	{
		synchronized (_listeners)
		{
			return _listeners.size();
		}
	}

	/**
	 * Gets a specific listener based on it's index (or current call order) in the
	 * listener stack. Listeners can change indices whenever new listeners are added
	 * or removed,
	 *
	 * @return The listener at the specified index in the listener list.
	 */
	public T getListener(int index)
	{
		synchronized (_listeners)
		{
			return _listeners.get(index);
		}
	}

	/**
	 * Gets the current index of the specified listener. Listeners are called based
	 * on their index, where the index is equal to the number of other listeners
	 * that will be called before this one. A listener's index can change at any
	 * time as listeners are added or removed from this event caller.
	 *
	 * @return The index of this listener, or -1 if it is not currently attached to
	 *         this event caller.
	 */
	public int getListenerIndex(T listener)
	{
		if (listener == null)
			return -1;

		synchronized (_listeners)
		{
			return _listeners.indexOf(listener);
		}
	}

	/**
	 * When this method is called, all currently pending events are sent to the
	 * currently listeners from this thread. This method should be called from the
	 * main thread to ensure that thread syncrhonization is occuring correctly. If
	 * this event caller has no currently pending events, nothing happens.
	 */
	public void handlePendingEvents()
	{
		while (true)
		{
			PendingEvent e;

			synchronized (_pendingEvents)
			{
				if (_pendingEvents.isEmpty())
					return;

				e = _pendingEvents.removeFirst();
			}

			handleEvent(e.getEventId(), e.getEventArg());
			_eventPool.put(e);
		}
	}

	private void handleEvent(int id, Object arg)
	{
		synchronized (_listeners)
		{
			_isInEvent = true;

			for (T t : _listeners)
			{
				try
				{
					runEvent(t, id, arg);
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

	/**
	 * A shorthand method for calling <code>callEvent(index, null)</code>.
	 *
	 * @see {@link #callEvent(int, Object)}
	 */
	protected void callEvent(int index)
	{
		callEvent(index, null);
	}

	/**
	 * Calls an event based on an index. This method will simply call
	 * {@link #runEvent(Listener, int, Object[])} for each listener currently
	 * attached to this event caller. This method handles common event caller issues
	 * such as error catching, concurrent modification, and disposal after event
	 * finished. It is recommended to use this wrapper method instead of calling
	 * listeners directly.<br>
	 * <br>
	 * If this EventCaller is handling event synchronization, events are added to an
	 * event queue internally which will be executed on the main thread. If the
	 * current thread is the main thread, all events in the queue are executed
	 * instantly.
	 *
	 * @param index
	 *            - The index of the evebt, This value is not used, and is mereley
	 *            the event index passed to each call of
	 *            {@link #runEvent(Listener, int, Object)}.
	 * @param arg
	 *            - The argument list of parameters to pass to the runEvent method.
	 */
	protected void callEvent(int index, Object arg)
	{
		if (_mainThread == null)
		{
			handleEvent(index, arg);
			return;
		}

		PendingEvent e = _eventPool.get().build(index, arg);

		synchronized (_pendingEvents)
		{
			_pendingEvents.add(e);
		}

		if (Thread.currentThread() == _mainThread)
			handlePendingEvents();
	}

	/**
	 * Calls an event based on an index. This method will simply call
	 * {@link #runEvent(Listener, int, Object[])} for each listener currently
	 * attached to this event caller. This method handles common event caller issues
	 * such as error catching, concurrent modification, and disposal after event
	 * finished. It is recommended to use this wrapper method instead of calling
	 * listeners directly.<br>
	 * <br>
	 * If this EventCaller is handling event synchronization, events are added to an
	 * event queue internally which will be executed on the main thread. If the
	 * current thread is the main thread, all events in the queue are executed
	 * instantly.
	 *
	 * @param index
	 *            - The index of the evebt, This value is not used, and is mereley
	 *            the event index passed to each call of
	 *            {@link #runEvent(Listener, int, Object)}.
	 * @param arg
	 *            - The argument list of parameters to pass to the runEvent method.
	 * @param block
	 *            - If true, this method will block on all non-main threads until
	 *            the event is handled. (NOT YET IMPLEMENTED!)
	 */
	protected void callEvent(int index, Object arg, boolean block)
	{
		// TODO Allow events to block until they are handled.

		callEvent(index, arg);
	}

	/**
	 * This method is called by @{@link #callEvent(int, Object)} for each listener
	 * attached to this event caller. This method should be used for the purpose of
	 * physically calling events on the given listener based on the event index.
	 *
	 * @param listener
	 *            - The listener to call the event method for.
	 * @param index
	 *            - The event index, provided by @{@link #callEvent(int, Object)},
	 *            as to indicate which event should be called.
	 * @param arg
	 *            - The argument parameters to pass along to the listener.
	 */
	protected abstract void runEvent(T listener, int index, Object arg);
}
