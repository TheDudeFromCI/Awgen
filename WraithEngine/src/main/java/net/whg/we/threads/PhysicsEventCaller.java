package net.whg.we.threads;

import net.whg.we.event.EventCallerBase;
import net.whg.we.event.Listener;
import net.whg.we.main.Plugin;
import net.whg.we.threads.PhysicsEventCaller.PhysicsListener;
import whg.core.CorePlugin;

/**
 * Represents an event caller for handling rendering events.
 *
 * @author TheDudeFromCI
 */
public class PhysicsEventCaller extends EventCallerBase<PhysicsListener>
{
	private static final int ON_PHYSICS_INIT_EVENT = 0;
	private static final int ON_UPDATE_EVENT = 1;
	private static final int ON_LATE_UPDATE_EVENT = 2;
	private static final int ON_PHYSICS_DISPOSE_EVENT = 3;
	private static final int ON_INPUT_UPDATE_EVENT = 4;

	/**
	 * Handles rendering events.
	 *
	 * @author TheDudeFromCI
	 */
	public static interface PhysicsListener extends Listener
	{
		/**
		 * Called when the physics thread is first initialized, before the game loop
		 * starts.
		 */
		public void onPhysicsInit();

		/**
		 * Called once each physics frame. Frames are called by the physics framerate as
		 * defined by {@link net.whg.we.utils.Time#getPhysicsFramerate()}.
		 */
		public void onUpdate();

		/**
		 * Called once each physics frame after {@link #onUpdate()} is finished.
		 */
		public void onLateUpdate();

		/**
		 * Called after the physics loop is disposed.
		 */
		public void onPhysicsDispose();

		/**
		 * Called once every input update frame. Frames are called by the input
		 * framerate as defined by {@link net.whg.we.utils.Time#getInputFramerate()}.
		 */
		public void onInputUpdateEvent();
	}

	private CorePlugin _core;

	PhysicsEventCaller(CorePlugin core)
	{
		_core = core;
	}

	@Override
	public String getName()
	{
		return CorePlugin.PHYSICS_EVENTCALLER;
	}

	@Override
	public Plugin getPlugin()
	{
		return _core;
	}

	void onPhysicsInit()
	{
		callEvent(ON_PHYSICS_INIT_EVENT);
	}

	void onUpdate()
	{
		callEvent(ON_UPDATE_EVENT);
	}

	void onLateUpdate()
	{
		callEvent(ON_LATE_UPDATE_EVENT);
	}

	void onPhysicsDispose()
	{
		callEvent(ON_PHYSICS_DISPOSE_EVENT);
	}

	void onInputUpdate()
	{
		callEvent(ON_INPUT_UPDATE_EVENT);
	}

	@Override
	protected void runEvent(PhysicsListener t, int index, Object[] args)
	{
		switch (index)
		{
			case ON_PHYSICS_INIT_EVENT:
				t.onPhysicsInit();
				break;

			case ON_UPDATE_EVENT:
				t.onUpdate();
				break;

			case ON_LATE_UPDATE_EVENT:
				t.onLateUpdate();
				break;

			case ON_PHYSICS_DISPOSE_EVENT:
				t.onPhysicsDispose();
				break;

			case ON_INPUT_UPDATE_EVENT:
				t.onInputUpdateEvent();
				break;

			default:
				throw new RuntimeException("Unknown event!");
		}
	}
}
