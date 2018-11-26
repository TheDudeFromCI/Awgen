package whg.WraithEngine;

import org.lwjgl.opengl.GL11;

import whg.WraithEngine.core.Screen;
import whg.WraithEngine.rendering.Camera;

public class DefaultWindowHandler implements WindowEventsHandler
{
	private Camera _camera;
	
	public DefaultWindowHandler(Camera camera)
	{
		_camera = camera;
	}

	@Override
	public void onWindowResized(Window window, int width, int height)
	{
		Screen.updateSize(width, height);
		_camera.rebuildProjectionMatrix();
		GL11.glViewport(0, 0, width, height);
	}
}
