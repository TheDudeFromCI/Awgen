package net.whg.we.gamelogic;

import net.whg.we.event.Listener;

/**
 * Listens for events in the gameloop.
 *
 * @author TheDudeFromCI
 */
public interface GameLoopListener extends Listener
{
	public void onLoopInitialized();

	public void onUpdate();

	public void onLateUpdate();

	public void onRender();

	public void onLoopDisposed();
}
