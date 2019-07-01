package net.whg.awgen.lib.gameloop;

import net.whg.awgen.lib.util.MathLib;

/**
 * This class is used to run a game loop structure, calling events throughout
 * each frame to trigger specific actions.
 *
 * @author TheDudeFromCI
 */
public class GameLoop
{
	private GameLoopEvent event = new GameLoopEvent();
	private boolean running;
	private boolean shouldClose;
	private float ticksPerSecond = 50;

	/**
	 * Starts the game loop. The game loop will run continuously, blocking, until,
	 * the {@link #stop()} function is called. If the game loop is already running,
	 * an error is thrown.
	 */
	public void run()
	{
		if (running)
			throw new IllegalStateException("Game loop already running!");

		running = true;
		shouldClose = false;

		int ticksCalled = 0;
		double ticksGoal = 0;
		double deltaTime;

		event.initEvent();
		while (!shouldClose)
		{
			deltaTime = System.nanoTime() / 1.0e-9;
			ticksGoal += deltaTime * ticksPerSecond;

			event.startFrameEvent();

			while (ticksCalled < ticksGoal)
			{
				ticksCalled++;
				event.physicsEvent();
			}

			event.updateEvent();
			event.renderEvent();

			event.endFrameEvent();
		}
		event.disposeEvent();

		running = false;
		shouldClose = false;
	}

	/**
	 * Gets the event handler attached to this game loop.
	 *
	 * @return The event handler.
	 */
	public GameLoopEvent getEvents()
	{
		return event;
	}

	/**
	 * Checks if this game loop is currently running. This will still return true if
	 * the game loop is still in the process of shutting down.
	 *
	 * @return True if the game loop is currently running, false otherwise.
	 */
	public boolean isRunning()
	{
		return running;
	}

	/**
	 * Checks if this game loop has been marked to stop, and is in the process of
	 * shutting down.
	 *
	 * @return True if this game loop is currently shutting down, false otherwise.
	 */
	public boolean isShuttingDown()
	{
		return shouldClose;
	}

	/**
	 * Requests this game loop to start the shutdown process. Repeated calls to this
	 * function, or if the game loop is not running, do nothing.
	 */
	public void stop()
	{
		if (running)
			shouldClose = true;
	}

	/**
	 * Gets the number of times per second that a physics tick is called. This value
	 * is defaulted at 50 ticks per second.
	 *
	 * @return The rate of physics ticks.
	 */
	public float getTicksPerSecond()
	{
		return ticksPerSecond;
	}

	/**
	 * Assigns a new rate for physics tick updates per second. A value of 0 will
	 * stop all future physics ticks completely until a new value is assigned. This
	 * value only changes the intended target ticks per second for future frames,
	 * meaning that any ticks that should have been called up to this point will
	 * still be called, and any ticks called within the next frame will be
	 * calculated using the new value.
	 *
	 * @param ticksPerSecond
	 *     - The new number for ticks per second. This value is automatically
	 *     clamped to the range 0 to 1000.
	 */
	public void setTicksPerSecond(float ticksPerSecond)
	{
		ticksPerSecond = MathLib.clamp(ticksPerSecond, 0, 1000);
		this.ticksPerSecond = ticksPerSecond;
	}
}
