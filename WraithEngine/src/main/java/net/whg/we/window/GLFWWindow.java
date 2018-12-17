package net.whg.we.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import net.whg.we.utils.Log;
import whg.WraithEngine.core.Time;
import whg.WraithEngine.utils.FPSLogger;

public class GLFWWindow implements Window
{
	public static final int WINDOW_UNINITIALIZED_STATE = -1;
	public static final int WINDOW_DISPOSED_STATE = 0;
	public static final int WINDOW_OPEN_STATE = 1;
	public static final int WINDOW_REQUESTING_CLOSE_STATE = 2;

	private String _name = "Untitled Window";
	private boolean _resizable = false;
	private int _windowState = WINDOW_UNINITIALIZED_STATE;
	private boolean _vSync = false;
	private int _width = 640;
	private int _height = 480;
	private long _windowId = 0;
	private Object _lock = new Object();

	@Override
	public void setName(String name)
	{
		Log.tracef("Tried to update window name to '%s' from '%s'.", name, _name);
		Log.tracef("  Window state is currently %s.", getWindowStateName());

		if (!canChangeName())
		{
			Log.warn("Failed to update window name.");
			return;
		}

		Log.debugf("Changed window name to '%s' from '%s'.", name, _name);
		_name = name;

		// TODO If window is already built, update window name
	}

	private String getWindowStateName()
	{
		switch (_windowState)
		{
			case WINDOW_UNINITIALIZED_STATE:
				return "Uninitialized";
			case WINDOW_DISPOSED_STATE:
				return "Disposed";
			case WINDOW_OPEN_STATE:
				return "Open";
			case WINDOW_REQUESTING_CLOSE_STATE:
				return "Requesting Close";
			default:
				return "Unknown";
		}
	}

	@Override
	public boolean canChangeName()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public void setResizable(boolean resizable)
	{
		Log.tracef("Tried to update window isResizable to '%s' from '%s'.", resizable, _resizable);
		Log.tracef("  Window state is currently %s.", getWindowStateName());

		if (!canChangeResizable())
		{
			Log.warn("Failed to update window isResizable.");
			return;
		}

		Log.debugf("Changed window isResizable to '%s' from '%s'.", resizable, _resizable);
		_resizable = resizable;
	}

	@Override
	public boolean canChangeResizable()
	{
		return !isWindowOpen();
	}

	@Override
	public boolean isResizable()
	{
		return _resizable;
	}

	@Override
	public void setVSync(boolean vSync)
	{
		Log.tracef("Tried to update window vsync to '%s' from '%s'.", vSync, _vSync);
		Log.tracef("  Window state is currently %s.", getWindowStateName());

		if (!canChangeVSync())
		{
			Log.warn("Failed to update window vsync.");
			return;
		}

		Log.debugf("Changed window vsync to '%s' from '%s'.", vSync, _vSync);
		_vSync = vSync;
	}

	@Override
	public boolean canChangeVSync()
	{
		return !isWindowOpen();
	}

	public int getWindowState()
	{
		return _windowState;
	}

	public boolean isWindowOpen()
	{
		return _windowState == WINDOW_OPEN_STATE || _windowState == WINDOW_REQUESTING_CLOSE_STATE;
	}

	@Override
	public boolean isVSync()
	{
		return _vSync;
	}

	@Override
	public void setWindowSize(int width, int height)
	{
		Log.tracef("Tried to update window size to %dx%d.", width, height);
		Log.tracef("  Window state is currently %s.", getWindowStateName());

		if (!canChangeWindowSize())
		{
			Log.warn("Failed to update window size.");
			return;
		}

		Log.debugf("Changed window size to %dx%d.", width, height);
		_width = width;
		_height = height;
	}

	@Override
	public boolean canChangeWindowSize()
	{
		return !isWindowOpen();
	}

	@Override
	public int getWidth()
	{
		return _width;
	}

	@Override
	public int getHeight()
	{
		return _height;
	}

	@Override
	public void buildWindow()
	{
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW!");

		_windowState = WINDOW_OPEN_STATE;

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		if (!_resizable)
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		_windowId = GLFW.glfwCreateWindow(_width, _height, _name, MemoryUtil.NULL, MemoryUtil.NULL);
		if (_windowId == 0)
			throw new RuntimeException("Failed to create GLFW window!");

		// assignCallbacks();

		try (MemoryStack stack = MemoryStack.stackPush())
		{
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			int x = (vidmode.width() - _width) / 2;
			int y = (vidmode.height() - _height) / 2;
			GLFW.glfwSetWindowPos(_windowId, x, y);
		}

		GLFW.glfwShowWindow(_windowId);
	}

	public void loop()
	{
		if (_windowState != WINDOW_OPEN_STATE)
			return;

		Thread t = new Thread(() ->
		{
			GLFW.glfwMakeContextCurrent(_windowId);

			if (_vSync)
				GLFW.glfwSwapInterval(1);
			else
				GLFW.glfwSwapInterval(0);

			GL.createCapabilities();

			GL11.glClearColor(0.2f, 0.4f, 0.8f, 1f);

			FPSLogger fps = new FPSLogger();
			while (_windowState == WINDOW_OPEN_STATE)
			{
				Time.updateTime();
				fps.logFramerate();

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				synchronized (_lock)
				{
					GLFW.glfwSwapBuffers(_windowId);
					GLFW.glfwPollEvents();
				}
			}
		});
		t.setName("Rendering");
		t.start();

		while (!GLFW.glfwWindowShouldClose(_windowId))
		{
			GLFW.glfwWaitEvents();
			synchronized (_lock)
			{
				if (_windowState == WINDOW_REQUESTING_CLOSE_STATE)
					GLFW.glfwSetWindowShouldClose(_windowId, true);
			}
		}

		_windowState = WINDOW_REQUESTING_CLOSE_STATE;
	}

	// private void assignCallbacks()
	// {
	// GLFW.glfwSetWindowSizeCallback(_windowId,
	// (long window, int width, int height) ->
	// {
	// _eventQueue.addEvent(
	// new WindowResizeEvent(this, width, height));
	// });
	//
	// GLFW.glfwSetKeyCallback(_windowId,
	// (long window, int key, int scancode, int action, int mods) ->
	// {
	// _eventQueue.addEvent(
	// new KeyPressedEvent(this, key, action, mods));
	// });
	//
	// GLFW.glfwSetCursorPosCallback(_windowId,
	// (long window, double mouseX, double mouseY) ->
	// {
	// synchronized (_mouseMoveEvent)
	// {
	// _mouseMoveEvent.setMousePos((float) mouseX,
	// (float) mouseY);
	// }
	// });
	// }

	@Override
	public void disposeWindow()
	{
		if (!isWindowOpen())
			return;

		synchronized (_lock)
		{
			Callbacks.glfwFreeCallbacks(_windowId);
			GLFW.glfwDestroyWindow(_windowId);
			_windowId = MemoryUtil.NULL;
		}

		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();

		_windowState = WINDOW_DISPOSED_STATE;
	}
}
