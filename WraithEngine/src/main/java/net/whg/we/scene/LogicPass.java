package net.whg.we.scene;

import net.whg.we.utils.ComponentList;

public class LogicPass
{
	private ComponentList<ComponentLogic> _components = new ComponentList<>();
	private boolean _running;

	public void addComponent(ComponentLogic logic)
	{
		if (logic == null)
			return;

		if (_running)
			_components.add(logic);
		else
			_components.addInstant(logic);

		logic.init();
	}

	public void removeComponent(ComponentLogic logic)
	{
		if (logic == null)
			return;

		if (_running)
			_components.remove(logic);
		else
			_components.removeInstant(logic);

		logic.init();
	}

	public void update()
	{
		try
		{
			_running = true;
			for (ComponentLogic logic : _components)
				logic.update();
			_components.endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	public void updateFrame()
	{
		try
		{
			_running = true;
			for (ComponentLogic logic : _components)
				logic.updateFrame();
			_components.endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	public void dispose()
	{
		try
		{
			_running = true;
			for (ComponentLogic logic : _components)
				logic.dispose();
			_components.clearPending();
		}
		finally
		{
			_running = false;
		}
	}
}
