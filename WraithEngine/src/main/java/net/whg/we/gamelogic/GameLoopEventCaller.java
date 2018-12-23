package net.whg.we.gamelogic;

import net.whg.we.event.EventCallerBase;
import net.whg.we.main.CorePlugin;
import net.whg.we.main.Plugin;

public class GameLoopEventCaller extends EventCallerBase<GameLoopListener>
{
	public static final String EVENT_CALLER_NAME = "GameLoop";
	public static final int LOOP_INITIALIZED_EVENT = 0;
	public static final int LOOP_DISPOSED_EVENT = 1;

	private CorePlugin _core;

	public GameLoopEventCaller(CorePlugin core)
	{
		_core = core;
	}

	@Override
	public String getName()
	{
		return EVENT_CALLER_NAME;
	}

	@Override
	public Plugin getPlugin()
	{
		return _core;
	}

	public void onLoopInitialized()
	{
		callEvent(LOOP_INITIALIZED_EVENT);
	}

	public void onLoopDisposed()
	{
		callEvent(LOOP_DISPOSED_EVENT);
	}

	@Override
	protected void runEvent(GameLoopListener t, int index)
	{
		switch (index)
		{
			case LOOP_INITIALIZED_EVENT:
				t.onLoopInitialized();
				break;
			case LOOP_DISPOSED_EVENT:
				t.onLoopDisposed();
				break;
			default:
				throw new IllegalStateException();
		}
	}
}
