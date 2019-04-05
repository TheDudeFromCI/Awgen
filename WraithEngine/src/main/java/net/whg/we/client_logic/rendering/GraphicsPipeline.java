package net.whg.we.client_logic.rendering;

import net.whg.we.client_logic.utils.Screen;
import net.whg.we.client_logic.window.QueuedWindow;
import net.whg.we.client_logic.window.WindowBuilder;
import net.whg.we.legacy.DefaultWindowListener;

public class GraphicsPipeline
{
    private Graphics _graphics;
    private QueuedWindow _window;

    public GraphicsPipeline()
    {
        _graphics =
                GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
        _window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW)
                .setName("WraithEngine").setResizable(false).setSize(800, 600)
                .setVSync(false).setListener(new DefaultWindowListener())
                .setGraphicsEngine(_graphics).build();
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
