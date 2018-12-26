package whg.core;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.GraphicsFactory;

class GraphicsThread
{
	private Graphics _graphics;

	void build()
	{
		_graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
	}

	void start()
	{
		Thread thread = new Thread(() ->
		{
			// TODO
		});

		thread.setName("render");
		thread.setDaemon(false);
		thread.start();
	}

	Graphics getGraphics()
	{
		return _graphics;
	}
}
