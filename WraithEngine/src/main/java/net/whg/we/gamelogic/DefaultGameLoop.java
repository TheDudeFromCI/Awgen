package net.whg.we.gamelogic;

import net.whg.we.event.EventManager;
import net.whg.we.main.CorePlugin;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;

public class DefaultGameLoop implements GameLoop
{
	private CorePlugin _core;
	private GameLoopEventCaller _eventCaller;

	public DefaultGameLoop(CorePlugin core)
	{
		_core = core;
		_eventCaller = new GameLoopEventCaller(core);

		EventManager.registerEventCaller(_eventCaller);
	}

	@Override
	public void loop()
	{
		// Initialize
		Time.resetTime();
		FPSLogger.resetLogTimer();

		Log.trace("Starting default update loop.");
		_eventCaller.onLoopInitialized();

		while (true)
		{
			Time.updateTime();
			FPSLogger.logFramerate();

			_eventCaller.onUpdate();
			_eventCaller.onLateUpdate();
			_eventCaller.onRender();

			// End frame
			Input.endFrame();
			if (_core.getWindow().endFrame())
				break;
		}

		Log.debug("Disposing game.");
		_eventCaller.onLoopDisposed();

		_core.getGraphics().dispose();
	}
}
