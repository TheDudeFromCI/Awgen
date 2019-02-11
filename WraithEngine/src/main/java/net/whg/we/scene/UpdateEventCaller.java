package net.whg.we.scene;

import net.whg.we.event.EventCallerBase;
import net.whg.we.utils.logging.Log;

public class UpdateEventCaller extends EventCallerBase<UpdateListener>
{
	private static final int UPDATE_EVENT = 0;
	private static final int UPDATE_FRAME_EVENT = 1;

	public void onUpdate()
	{
		callEvent(UPDATE_EVENT);
	}

	public void onUpdateFrame()
	{
		callEvent(UPDATE_FRAME_EVENT);
	}

	@Override
	protected void runEvent(UpdateListener listener, int index, Object arg)
	{
		switch (index)
		{
			case UPDATE_EVENT:
				listener.onUpdate();
				break;

			case UPDATE_FRAME_EVENT:
				listener.onUpdateFrame();
				break;

			default:
				Log.warn("Unknown event index!");
		}
	}
}
