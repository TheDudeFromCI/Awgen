package whg.WraithEngine;

import whg.WraithEngine.gamelogic.RenderLoop;

public class WindowResizeEvent implements GLFWEvent
{
	private Window _window;
	private int _width;
	private int _height;
	
	public WindowResizeEvent(Window window, int width, int height)
	{
		_window = window;
		_width = width;
		_height = height;
	}

	@Override
	public void handleEvent()
	{
		RenderLoop renderLoop = _window.getRenderLoop();
		if (renderLoop != null)
		{
			WindowEventsHandler handler = renderLoop.getWindowEventsHandler();
			if (handler != null)
				handler.onWindowResized(_window, _width, _height);
		}
	}
}
