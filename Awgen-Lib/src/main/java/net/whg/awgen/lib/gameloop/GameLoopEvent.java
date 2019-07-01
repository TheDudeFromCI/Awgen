package net.whg.awgen.lib.gameloop;

import net.whg.stlib.event.EventCallerBase;

/**
 * The game loop event executor class. This class is used to distribute all game
 * loop related events.
 *
 * @author TheDudeFromCI
 */
public class GameLoopEvent extends EventCallerBase<GameLoopListener>
{
	private static final int INIT_EVENT = 0;
	private static final int START_FRAME_EVENT = 1;
	private static final int PHYSICS_EVENT = 2;
	private static final int UPDATE_EVENT = 3;
	private static final int RENDER_EVENT = 4;
	private static final int END_FRAME_EVENT = 5;
	private static final int DISPOSE_EVENT = 6;

	void initEvent()
	{
		callEvent(INIT_EVENT);
	}

	void startFrameEvent()
	{
		callEvent(START_FRAME_EVENT);
	}

	void physicsEvent()
	{
		callEvent(PHYSICS_EVENT);
	}

	void updateEvent()
	{
		callEvent(UPDATE_EVENT);
	}

	void renderEvent()
	{
		callEvent(RENDER_EVENT);
	}

	void endFrameEvent()
	{
		callEvent(END_FRAME_EVENT);
	}

	void disposeEvent()
	{
		callEvent(DISPOSE_EVENT);
	}

	@Override
	protected void runEvent(GameLoopListener listener, int index, Object arg)
	{
		switch (index)
		{
			case INIT_EVENT:
				listener.onInit();
				break;

			case START_FRAME_EVENT:
				listener.onStartFrame();
				break;

			case PHYSICS_EVENT:
				listener.onPhysics();
				break;

			case UPDATE_EVENT:
				listener.onUpdate();
				break;

			case RENDER_EVENT:
				listener.onRender();
				break;

			case END_FRAME_EVENT:
				listener.onEndFrame();
				break;

			case DISPOSE_EVENT:
				listener.onDispose();
				break;
		}
	}
}
