package net.whg.we.scene;

import net.whg.we.event.EventCallerBase;
import net.whg.we.utils.Log;

public class UpdateEventCaller extends EventCallerBase<UpdateListener>
{
	private static final int UPDATE_EVENT = 0;

	public void onUpdate()
	{
		callEvent(UPDATE_EVENT);
	}

	protected void runEvent(UpdateListener listener, int index, Object[] args)
	{
		switch(index)
		{
			case UPDATE_EVENT:
				listener.onUpdate();
				break;

			default:
				Log.warn("Unknown event index!");
		}
	}
}
