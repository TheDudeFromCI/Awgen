package whg.WraithEngine.window;

import java.util.LinkedList;

public class GLFWEventQueue
{
	private LinkedList<GLFWEvent> _events;

	public GLFWEventQueue()
	{
		_events = new LinkedList<>();
	}

	public void addEvent(GLFWEvent event)
	{
		synchronized (_events)
		{
			_events.add(event);
		}
	}

	public void handleEvents()
	{
		synchronized (_events)
		{
			for (GLFWEvent e : _events)
				e.handleEvent();
			_events.clear();
		}
	}

	public void clearEvents()
	{
		synchronized (_events)
		{
			_events.clear();
		}
	}
}
