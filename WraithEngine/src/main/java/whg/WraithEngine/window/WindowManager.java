package whg.WraithEngine.window;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class WindowManager
{
	private ArrayList<Window> _windows;
	private boolean _initalized;
	
	public WindowManager()
	{
		_windows = new ArrayList<>();
	}
	
	public void init()
	{
		if (_initalized)
			return;
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW!");
		
		_initalized = true;
	}
	
	public void dispose()
	{
		if (!_initalized)
			return;
		
		for (Window w : _windows)
			w.destroy();
		_windows.clear();
		
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();

		_initalized = false;
	}
	
	public Window createNewWindow()
	{
		if (!_initalized)
			throw new IllegalStateException("Window Manager has not yet been initalized!");

		Window window = new Window();
		_windows.add(window);
		
		return window;
	}
	
	public void removeWindow(Window window)
	{
		window.destroy();
		_windows.remove(window);
	}
}
