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

import net.whg.we.test.TestScene;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.Log;
import net.whg.we.utils.Time;
import net.whg.we.window.WindowBuilder;

public class GraphicsPipeline
{
	private Graphics _graphics;
	private QueuedWindow _window;
	private ResourceLoader _resourceLoader;

	public GraphicsPipeline(ResourceLoader resourceLoader)
	{
		_resourceLoader = resourceLoader;

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

	private void dispose()
	{
		_resourceLoader.disposeResources();
		_graphics.dispose();
	}

	public void run()
	{
		TestScene testScene = new TestScene();
		testScene.loadTestScene(_resourceLoader, _graphics, _window);

		while (true)
		{
			try
			{
				// Calculate frame data
				Time.updateTime();
				FPSLogger.logFramerate();

				testScene.updateTestScene(_graphics, _window);
				testScene.renderTestScene(_graphics, _window);

				// End frame
				Input.endFrame();
				if (_window.endFrame())
					break;
			}
			catch (Exception exception)
			{
				Log.fatalf("Fatal error thrown in main thread!", exception);
				requestClose();
				break;
			}
		}

		dispose();
	}
}
