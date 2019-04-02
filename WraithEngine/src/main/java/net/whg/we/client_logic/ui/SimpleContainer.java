package net.whg.we.client_logic.ui;

import net.whg.frameworks.util.ComponentList;
import net.whg.we.scene.Transform2D;

public abstract class SimpleContainer implements UIContainer
{
	private ComponentList<UIComponent> _components = new ComponentList<>();
	private Transform2D _transform = new Transform2D();
	private boolean _running;
	private boolean _disposed;

	@Override
	public Transform2D getTransform()
	{
		return _transform;
	}

	@Override
	public void init()
	{
		try
		{
			_running = true;
			for (UIComponent component : _components)
				component.init();
			_components.endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	@Override
	public void update()
	{
		try
		{
			_running = true;
			for (UIComponent component : _components)
				component.update();
			_components.endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	@Override
	public void updateFrame()
	{
		try
		{
			_running = true;
			for (UIComponent component : _components)
				component.updateFrame();
			_components.endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	@Override
	public void render()
	{
		try
		{
			_running = true;
			for (UIComponent component : _components)
				component.render();
			_components.endFrame();
		}
		finally
		{
			_running = false;
		}
	}

	@Override
	public void dispose()
	{
		try
		{
			_running = true;
			_disposed = true;
			for (UIComponent component : _components)
				component.dispose();
			_components.clearPending();
		}
		finally
		{
			_running = false;
		}
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}

	@Override
	public void addComponent(UIComponent component)
	{
		if (component == null)
			return;

		if (_running)
			_components.add(component);
		else
			_components.addInstant(component);

		component.getTransform().setParent(getTransform());
	}

	@Override
	public void removeComponent(UIComponent component)
	{
		if (component == null)
			return;

		if (_running)
			_components.remove(component);
		else
			_components.removeInstant(component);

		component.getTransform().setParent(null);
		component.dispose();
	}

}
