package net.whg.we.gamelogic;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;
import net.whg.we.window.QueuedWindow;

public class DefaultGameLoop implements GameLoop
{
	private boolean _shouldClose;
	private QueuedWindow _window;

	public DefaultGameLoop(QueuedWindow window)
	{
		_window = window;
	}

	@Override
	public void loop()
	{
		_shouldClose = false;

		Log.trace("Setting default OpenGL clear color. (0.2f, 0.4f, 0.8f, 1f)");
		GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);

		Time.resetTime();
		FPSLogger.resetLogTimer();

		Log.trace("Starting default update loop.");
		while (!_shouldClose)
		{
			Time.updateTime();
			FPSLogger.logFramerate();

			float v = (float) Math.sin(Time.time() * 4f) * 0.5f + 0.5f;
			GL11.glClearColor(v, v, v, 1f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			if (_window.endFrame())
				break;
		}

		Log.debug("Disposing game.");
		// Nothing to dispose.

		Log.debug("Disposing OpenGL.");
		GL.setCapabilities(null);
	}

	@Override
	public void requestClose()
	{
		_shouldClose = true;
		_window.requestClose();
	}
}
