package net.whg.awgen.lib.util;

import net.whg.awgen.lib.gameloop.GameLoopAdapter;

/**
 * A very small, simple utility for adding a 1ms thread delay on frames within a
 * game loop where the physics update does not occur. This is useful in server
 * environments where only the physics event is needed, to prevent maxing thread
 * usage between physics updates.
 *
 * @author TheDudeFromCI
 */
public class GameLoopPhysicsYield extends GameLoopAdapter
{
	private boolean physicsCalled;
	private boolean enabled = true;

	@Override
	public void onStartFrame()
	{
		physicsCalled = false;
	}

	@Override
	public void onPhysics()
	{
		physicsCalled = true;
	}

	@Override
	public void onEndFrame()
	{
		if (!physicsCalled && enabled)
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
			}
	}

	/**
	 * Checks if this listener is currently enabled. If not enabled, this listener
	 * does not preform any yield events, regardless of whether physics events were
	 * called or not.
	 *
	 * @return True if this object is enabled, false otherwise.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Assigns whether this object should be enabled or not.
	 *
	 * @param enabled
	 *     - The state to assign.
	 * @see {@link #isEnabled()}
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
