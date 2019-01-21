package net.whg.we.scene;

import net.whg.we.event.EventCaller;

public interface GameLoop
{
	public void run();

	public void requestClose();

	public EventCaller<UpdateListener> getUpdateEvent();
}
