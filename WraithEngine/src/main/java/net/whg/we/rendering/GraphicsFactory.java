package net.whg.we.rendering;

import net.whg.we.rendering.opengl.OpenGLGraphics;

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
