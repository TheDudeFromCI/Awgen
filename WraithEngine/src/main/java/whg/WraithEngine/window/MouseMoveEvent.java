package whg.WraithEngine.window;

public class MouseMoveEvent implements GLFWEvent
{
	private Window _window;
	private float _mouseX;
	private float _mouseY;

	public MouseMoveEvent(Window window)
	{
		_window = window;
	}

	public void setMousePos(float mouseX, float mouseY)
	{
		_mouseX = mouseX;
		_mouseY = mouseY;
	}

	@Override
	public void handleEvent()
	{
		_window.getRenderLoop().getMouseEventsHandler().onMouseMoved(_mouseX,
				_mouseY);
	}
}
