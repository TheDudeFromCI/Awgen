package whg.WraithEngine.window;

import org.lwjgl.glfw.GLFW;
import net.whg.we.utils.Input;

public class DefaultInputHandler
		implements KeyboardEventsHandler, MouseEventsHandler
{
	@Override
	public void onKeyPressed(int key, int action, int mods)
	{
		boolean pressed;
		switch (action)
		{
			case GLFW.GLFW_PRESS:
				pressed = true;
				break;
			case GLFW.GLFW_RELEASE:
				pressed = false;
				break;
			default:
				return;
		}

		Input.setKeyPressed(key, pressed);
	}

	@Override
	public void onMouseMoved(float mouseX, float mouseY)
	{
		Input.setMousePosition(mouseX, mouseY);
	}
}
