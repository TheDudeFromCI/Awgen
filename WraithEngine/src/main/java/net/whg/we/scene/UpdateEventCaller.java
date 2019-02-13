package net.whg.we.scene;

import net.whg.we.event.EventCallerBase;
import net.whg.we.utils.logging.Log;

public class UpdateEventCaller extends EventCallerBase<UpdateListener>
{
	private static final int INIT_EVENT = 0;
	private static final int UPDATE_EVENT = 1;
	private static final int UPDATE_FRAME_EVENT = 2;
	private static final int RENDER_EVENT = 3;
	private static final int DISPOSE_EVENT = 4;

	public void init()
	{
		callEvent(INIT_EVENT);
	}

	public void onUpdate()
	{
		callEvent(UPDATE_EVENT);
	}

	public void onUpdateFrame()
	{
		callEvent(UPDATE_FRAME_EVENT);
	}

	public void render()
	{
		callEvent(RENDER_EVENT);
	}

	@Override
	public void dispose()
	{
		callEvent(DISPOSE_EVENT);
	}

	@Override
	protected void runEvent(UpdateListener listener, int index, Object arg)
	{
		switch (index)
		{
			case INIT_EVENT:
				listener.init();
				break;

			case UPDATE_EVENT:
				listener.update();
				break;

			case UPDATE_FRAME_EVENT:
				listener.updateFrame();
				break;

			case RENDER_EVENT:
				listener.render();
				break;

			case DISPOSE_EVENT:
				listener.dispose();
				break;

			default:
				Log.warn("Unknown event index!");
		}
	}
}
