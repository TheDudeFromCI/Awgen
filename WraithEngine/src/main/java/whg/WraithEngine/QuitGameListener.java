package whg.WraithEngine;

import whg.WraithEngine.core.Input;

public class QuitGameListener implements Entity, Updateable
{
	private Window _window;
	private String _key;
	
	public QuitGameListener(Window window, String key)
	{
		_window = window;
		_key = key;
	}

	@Override
	public void update()
	{
		if (Input.isKeyDown(_key))
			_window.requestClose();
	}

	@Override
	public Location getLocation()
	{
		return null;
	}
}
