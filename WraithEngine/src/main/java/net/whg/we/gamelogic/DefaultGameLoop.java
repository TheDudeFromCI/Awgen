package net.whg.we.gamelogic;

import net.whg.we.main.CorePlugin;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.utils.Color;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;

public class DefaultGameLoop implements GameLoop
{
	private boolean _shouldClose;
	private CorePlugin _core;

	public DefaultGameLoop(CorePlugin core)
	{
		_core = core;
	}

	@Override
	public void loop()
	{
		_shouldClose = false;

		Time.resetTime();
		FPSLogger.resetLogTimer();
		Color color = new Color();

		Log.trace("Starting default update loop.");
		while (!_shouldClose)
		{
			Time.updateTime();
			FPSLogger.logFramerate();

			color.setLumosity((float) Math.sin(Time.time() * 4f) * 0.5f + 0.5f);
			_core.getGraphics().setClearScreenColor(color);
			_core.getGraphics().clearScreenPass(ScreenClearType.CLEAR_COLOR);

			if (_core.getWindow().endFrame())
				break;
		}

		Log.debug("Disposing game.");
		// Nothing to dispose.

		_core.getGraphics().dispose();
	}

	@Override
	public void requestClose()
	{
		_shouldClose = true;
		_core.getWindow().requestClose();
	}
}
