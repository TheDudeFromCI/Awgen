package net.whg.we.rendering;

import java.util.ArrayList;

public class RenderGroup
{
	private ArrayList<Renderable> _renderables = new ArrayList<>();

	public void addRenderable(Renderable renderable)
	{
		if (_renderables.contains(renderable))
			return;

		_renderables.add(renderable);
	}

	public void removeRenderable(Renderable renderable)
	{
		_renderables.remove(renderable);
	}

	public void render(Camera camera)
	{
		for (Renderable r : _renderables)
			r.render(camera);
	}

	public void forEach(RenderableAction action)
	{
		for (Renderable r : _renderables)
			action.run(r);
	}

	public void clear()
	{
		_renderables.clear();
	}

	public static interface RenderableAction
	{
		public void run(Renderable r);
	}
}
