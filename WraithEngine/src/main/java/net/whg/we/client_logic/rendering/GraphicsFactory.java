package net.whg.we.client_logic.rendering;

import net.whg.we.client_logic.rendering.opengl.OpenGLGraphics;

public class GraphicsFactory
{
    public static final int OPENGL_ENGINE = 0;

    public static Graphics createInstance(int engine)
    {
        switch (engine)
        {
            case OPENGL_ENGINE:
                return new OpenGLGraphics();
            default:
                throw new IllegalArgumentException("Unknown graphics engine!");
        }
    }
}
