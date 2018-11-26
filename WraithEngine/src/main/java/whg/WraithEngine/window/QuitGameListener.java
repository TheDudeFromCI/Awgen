package whg.WraithEngine.window;

import whg.WraithEngine.core.Input;
import whg.WraithEngine.gamelogic.Entity;
import whg.WraithEngine.gamelogic.Location;
import whg.WraithEngine.gamelogic.Updateable;

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
