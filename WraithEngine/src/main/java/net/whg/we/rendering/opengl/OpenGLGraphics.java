package net.whg.we.rendering.opengl;

import org.lwjgl.opengl.GL;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.VMesh;
import whg.WraithEngine.rendering.VertexData;

public class OpenGLGraphics implements Graphics
{
	@Override
	public void init()
	{
		GL.createCapabilities();
	}

	@Override
	public VMesh prepareMesh(VertexData vertexData)
	{
		return new GLVMesh(vertexData);
	}
}
