package net.whg.we.client_logic.ui;

import net.whg.we.utils.ComponentList;
import net.whg.we.utils.ObjectPool;
import net.whg.we.utils.Poolable;

public class UIStack
{
	private ComponentList<UIComponent> _components = new ComponentList<>();
	private ObjectPool<ComponentAction> _componentActions =
			new ObjectPool<UIStack.ComponentAction>()
			{
				@Override
				protected ComponentAction build()
				{
					return new ComponentAction();
				}
			};
	private boolean _running = false;

	public void addComponent(UIComponent component)
	{
		if (component == null)
			return;

		if (!_running)
			_components.addInstant(component);
		else
			_components.add(component);

		component.init();
	}

	public void removeComponent(UIComponent component)
	{
		if (component == null)
			return;

		if (!_running)
			_components.removeInstant(component);
		else
			_components.remove(component);

		if (!component.isDisposed())
			component.dispose();
	}

	public void endFrame()
	{
		_components.endFrame();
	}

	public void update()
	{
		try
		{
			_running = true;
			for (UIComponent component : _components)
			{
				if (component.isDisposed())
					removeComponent(component);
				else
					component.update();
			}
			endFrame();
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
			for (UIComponent component : _components)
			{
				if (component.isDisposed())
					removeComponent(component);
				else
					component.updateFrame();
			}
			endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	public void render()
	{
		try
		{
			_running = true;
			for (UIComponent component : _components)
			{
				if (component.isDisposed())
					removeComponent(component);
				else
					component.render();
			}
			endFrame();
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
			for (UIComponent component : _components)
			{
				if (!component.isDisposed())
					component.dispose();
			}
			_components.clearPending();
		}
		finally
		{
			_running = false;
		}
	}

	public void sendToFront(UIComponent comp)
	{
		if (!_running)
		{
			ComponentAction action = _componentActions.get();
			action.set(comp, ComponentAction.SEND_TO_FRONT).run();
			_componentActions.put(action);
			return;
		}

		_components.preformComponentAction(
				_componentActions.get().set(comp, ComponentAction.SEND_TO_FRONT));
	}

	public void sendToBack(UIComponent comp)
	{
		if (!_running)
		{
			ComponentAction action = _componentActions.get();
			action.set(comp, ComponentAction.SEND_TO_BACK).run();
			_componentActions.put(action);
			return;
		}

		_components.preformComponentAction(
				_componentActions.get().set(comp, ComponentAction.SEND_TO_BACK));
	}

	public void moveForward(UIComponent comp)
	{
		if (!_running)
		{
			ComponentAction action = _componentActions.get();
			action.set(comp, ComponentAction.MOVE_FORWARD).run();
			_componentActions.put(action);
			return;
		}

		_components.preformComponentAction(
				_componentActions.get().set(comp, ComponentAction.MOVE_FORWARD));
	}

	public void moveBackward(UIComponent comp)
	{
		if (!_running)
		{
			ComponentAction action = _componentActions.get();
			action.set(comp, ComponentAction.MOVE_BACKWARD).run();
			_componentActions.put(action);
			return;
		}

		_components.preformComponentAction(
				_componentActions.get().set(comp, ComponentAction.MOVE_BACKWARD));
	}

	public int size()
	{
		return _components.size();
	}

	public class ComponentAction implements Runnable, Poolable
	{
		private static final int SEND_TO_FRONT = 0;
		private static final int SEND_TO_BACK = 1;
		private static final int MOVE_FORWARD = 2;
		private static final int MOVE_BACKWARD = 3;

		private UIComponent _component;
		private int _action;

		@Override
		public void init()
		{
		}

		@Override
		public void dispose()
		{
			_component = null;
		}

		public ComponentAction set(UIComponent component, int action)
		{
			_component = component;
			_action = action;
			return this;
		}

		@Override
		public void run()
		{
			try
			{
				if (_component == null || _component.isDisposed())
					return;

				int compIndex = _components.indexOf(_component);

				if (compIndex == -1)
					return;

				switch (_action)
				{
					case SEND_TO_FRONT:
						_components.removeInstant(_component);
						_components.add(_components.size(), _component);
						break;

					case SEND_TO_BACK:
						_components.removeInstant(_component);
						_components.add(0, _component);
						break;

					case MOVE_FORWARD:
						if (compIndex < _components.size() - 1)
						{
							_components.removeInstant(_component);
							_components.add(compIndex + 1, _component);
						}
						break;

					case MOVE_BACKWARD:
						if (compIndex > 0)
						{
							_components.removeInstant(_component);
							_components.add(compIndex - 1, _component);
						}
						break;

					default:
						throw new IllegalStateException("Unknown component action!");
				}
			}
			finally
			{
				_componentActions.put(this);
			}
		}
	}
}
