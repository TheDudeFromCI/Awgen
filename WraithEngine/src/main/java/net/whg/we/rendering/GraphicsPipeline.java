package net.whg.we.rendering;

import net.whg.we.resources.ResourceLoader;
import net.whg.we.test.TestScene;
import net.whg.we.utils.DefaultWindowListener;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Screen;
import net.whg.we.utils.Time;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;

public class GraphicsPipeline
{
	private Graphics _graphics;
	private QueuedWindow _window;

	public GraphicsPipeline()
	{
		_graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		_window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW).setName("WraithEngine")
				.setResizable(false).setSize(800, 600).setVSync(false)
				.setListener(new DefaultWindowListener()).setGraphicsEngine(_graphics).build();
		_graphics.init();
		Screen.setWindow(_window);
	}

	public Graphics getGraphics()
	{
		return _graphics;
	}

	public QueuedWindow getWindow()
	{
		return _window;
	}

	public void requestClose()
	{
		_window.requestClose();
	}

	public void dispose()
	{
		_graphics.dispose();
	}
}
