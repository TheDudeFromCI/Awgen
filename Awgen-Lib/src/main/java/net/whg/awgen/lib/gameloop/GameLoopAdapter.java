package net.whg.awgen.lib.gameloop;

/**
 * A default pass-through class for easier implementation of the game loop
 * listener. This class can be extended and used as a normal game loop listener.
 *
 * @author TheDudeFromCI
 */
public abstract class GameLoopAdapter implements GameLoopListener
{
	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void onInit()
	{
	}

	@Override
	public void onStartFrame()
	{
	}

	@Override
	public void onPhysics()
	{
	}

	@Override
	public void onUpdate()
	{
	}

	@Override
	public void onRender()
	{
	}

	@Override
	public void onEndFrame()
	{
	}

	@Override
	public void onDispose()
	{
	}
}
