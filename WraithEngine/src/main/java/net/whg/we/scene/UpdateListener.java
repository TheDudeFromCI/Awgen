package net.whg.we.scene;

import net.whg.we.event.Listener;

public interface UpdateListener extends Listener
{
	public void onUpdate();

	public void onUpdateFrame();
}
