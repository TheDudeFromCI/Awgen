package net.whg.we.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogicPass
{
	private List<ComponentLogic> _logicalComponents = new ArrayList<>();
	private List<ComponentLogic> _toRemove = new LinkedList<>();

	public void addComponent(ComponentLogic logic)
	{
		if (_logicalComponents.contains(logic))
			return;

		_logicalComponents.add(logic);
	}

	public void removeComponent(ComponentLogic logic)
	{
		if (_toRemove.contains(logic))
			return;

		_toRemove.add(logic);
	}

	public void endFrame()
	{
		for (ComponentLogic logic : _toRemove)
			_logicalComponents.remove(logic);

		_toRemove.clear();
	}

	public void updatePass()
	{
		for (ComponentLogic logic : _logicalComponents)
			logic.update();
	}

	public void updateFramePass()
	{
		for (ComponentLogic logic : _logicalComponents)
			logic.updateFrame();
	}
}
