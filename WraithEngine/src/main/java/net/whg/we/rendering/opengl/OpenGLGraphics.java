package net.whg.we.rendering.opengl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.rendering.VMesh;
import net.whg.we.rendering.VertexData;
import net.whg.we.utils.Color;
import net.whg.we.utils.Log;

public class OpenGLGraphics implements Graphics
{
	@Override
	public void init()
	{
		GL.createCapabilities();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	@Override
	public VMesh prepareMesh(VertexData vertexData)
	{
		return new GLVMesh(vertexData);
	}

	@Override
	public void clearScreenPass(ScreenClearType screenClear)
	{
		switch (screenClear)
		{
			case CLEAR_COLOR:
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				return;
			case CLEAR_DEPTH:
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				return;
			case CLEAR_COLOR_AND_DEPTH:
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				return;
			default:
				throw new IllegalArgumentException("Unknown screen clear operation!");
		}
	}

	@Override
	public void setClearScreenColor(Color color)
	{
		GL11.glClearColor(color.r, color.g, color.b, color.a);
	}

	@Override
	public void dispose()
	{
		Log.debug("Disposing OpenGL.");
		GL.setCapabilities(null);
	}
}
