package whg.WraithEngine.window;

public class KeyPressedEvent implements GLFWEvent
{
	private Window _window;
	private int _key;
	private int _action;
	private int _mods;

	public KeyPressedEvent(Window window, int key, int action, int mods)
	{
		_window = window;
		_key = key;
		_action = action;
		_mods = mods;
	}

	@Override
	public void handleEvent()
	{
		_window.getRenderLoop().getKeyboardEventsHandler().onKeyPressed(_key,
				_action, _mods);
	}
}
