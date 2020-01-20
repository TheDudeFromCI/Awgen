package net.whg.client;

import net.whg.awgen.lib.util.Version;
import net.whg.we.external.GlfwApi;
import net.whg.we.external.OpenGLApi;
import net.whg.we.main.GameLoop;
import net.whg.we.main.Input;
import net.whg.we.rendering.IRenderingEngine;
import net.whg.we.rendering.IScreenClearHandler;
import net.whg.we.rendering.opengl.IOpenGL;
import net.whg.we.rendering.opengl.OpenGLRenderingEngine;
import net.whg.we.util.Color;
import net.whg.we.window.IWindow;
import net.whg.we.window.IWindowAdapter;
import net.whg.we.window.WindowSettings;
import net.whg.we.window.glfw.GlfwWindow;
import net.whg.we.window.glfw.IGlfw;

/**
 * The main entry point for the Awgen client.
 */
public class Awgen
{
    /**
     * The entry point for the Awgen client.
     * 
     * @param args
     *     - The arguments for configuring the launch properties.
     */
    public static void main(String[] args)
    {
        Version.get()
               .printInfo();

        IGlfw glfw = new GlfwApi();
        IOpenGL opengl = new OpenGLApi();

        IRenderingEngine renderingEngine = new OpenGLRenderingEngine(opengl);

        WindowSettings windowSettings = new WindowSettings();
        windowSettings.setTitle("Awgen");

        IWindow window = new GlfwWindow(glfw, renderingEngine, windowSettings);

        IScreenClearHandler screenClear = renderingEngine.getScreenClearHandler();
        screenClear.setClearColor(new Color(0.2f, 0.4f, 0.8f));

        GameLoop gameloop = new GameLoop();
        gameloop.addAction(() -> screenClear.clearScreen());
        gameloop.addAction(() -> Input.endFrame());
        gameloop.addAction(() -> window.pollEvents());

        window.addWindowListener(new IWindowAdapter()
        {
            @Override
            public void onWindowRequestClose(IWindow window)
            {
                gameloop.stop();
            }
        });

        gameloop.loop();
        window.dispose();
    }
}
