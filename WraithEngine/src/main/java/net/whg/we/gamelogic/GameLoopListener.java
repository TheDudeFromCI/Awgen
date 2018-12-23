package net.whg.we.gamelogic;

import net.whg.we.event.Listener;

public interface GameLoopListener extends Listener
{
	public void onLoopInitialized();

	public void onLoopDisposed();
}
