package whg.WraithEngine.rendering.hardware;

import org.lwjgl.opengl.GL;

public class OpenGLBridge implements GraphicsBridge
{
	@Override
	public void buildWindowContext()
	{
		GL.createCapabilities();
	}
}
