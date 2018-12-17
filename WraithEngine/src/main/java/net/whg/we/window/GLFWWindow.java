package net.whg.we.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class GLFWWindow implements Window
{
	private String _name = "WraithEngine Project";
	private boolean _resizable = false;
	private boolean _vSync = false;
	private int _width = 640;
	private int _height = 480;
	private long _windowId = 0;
	private QueuedWindow _window;
	private Object _lock = new Object();
	private float _mouseX;
	private float _mouseY;

	@Override
	public void setName(String name)
	{
		if (isWindowOpen())
			return;

		_name = name;

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setNameInstant(_name);
			});
	}

	@Override
	public void setResizable(boolean resizable)
	{
		if (isWindowOpen())
			return;

		_resizable = resizable;

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setResizableInstant(_resizable);
			});
	}

	@Override
	public void setVSync(boolean vSync)
	{
		if (isWindowOpen())
			return;

		_vSync = vSync;

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setVSyncInstant(_vSync);
			});
	}

	@Override
	public void setSize(int width, int height)
	{
		if (isWindowOpen())
			return;

		_width = width;
		_height = height;

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setSizeInstant(_width, _height);
			});
	}

	public boolean isWindowOpen()
	{
		return _windowId != MemoryUtil.NULL;
	}

	@Override
	public void buildWindow()
	{
		synchronized (_lock)
		{
			GLFWErrorCallback.createPrint(System.err).set();

			if (!GLFW.glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW!");

			GLFW.glfwDefaultWindowHints();
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

			if (!_resizable)
				GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

			_windowId =
					GLFW.glfwCreateWindow(_width, _height, _name, MemoryUtil.NULL, MemoryUtil.NULL);
			if (_windowId == 0)
				throw new RuntimeException("Failed to create GLFW window!");

			GLFW.glfwSetWindowSizeCallback(_windowId, (long window, int width, int height) ->
			{
				if (_window != null)
					_window.addEvent(() ->
					{
						_window.setSizeInstant(_width, _height);
					});
			});

			GLFW.glfwSetKeyCallback(_windowId,
					(long window, int key, int scancode, int action, int mods) ->
					{
						final KeyState state;

						if (action == GLFW.GLFW_PRESS)
							state = KeyState.PRESSED;
						else if (action == GLFW.GLFW_RELEASE)
							state = KeyState.RELEASED;
						else
							state = KeyState.REPEATED;

						if (_window != null)
							_window.addEvent(() ->
							{
								_window.onKey(key, state, mods);
							});
					});

			GLFW.glfwSetCursorPosCallback(_windowId, (long window, double mouseX, double mouseY) ->
			{
				_mouseX = (float) mouseX;
				_mouseY = (float) mouseY;
			});

			try (MemoryStack stack = MemoryStack.stackPush())
			{
				GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

				int x = (vidmode.width() - _width) / 2;
				int y = (vidmode.height() - _height) / 2;
				GLFW.glfwSetWindowPos(_windowId, x, y);
			}

			GLFW.glfwShowWindow(_windowId);
		}
	}

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

			GLFW.glfwTerminate();
			GLFW.glfwSetErrorCallback(null).free();
		}
	}

	@Override
	public void requestClose()
	{
		synchronized (_lock)
		{
			GLFW.glfwSetWindowShouldClose(_windowId, true);
		}

		if (_window != null)
			_window.addEvent(() ->
			{
				_window.setSizeInstant(_width, _height);
			});
	}

	@Override
	public boolean endFrame()
	{
		if (_window != null)
			_window.onMouseMove(_mouseX, _mouseY);

		synchronized (_lock)
		{
			GLFW.glfwSwapBuffers(_windowId);
			return GLFW.glfwWindowShouldClose(_windowId);
		}
	}

	@Override
	public void linkToOpenGL()
	{
		synchronized (_lock)
		{
			GLFW.glfwMakeContextCurrent(_windowId);
			GL.createCapabilities();

			if (_vSync)
				GLFW.glfwSwapInterval(1);
			else
				GLFW.glfwSwapInterval(0);
		}
	}

	@Override
	public void setQueuedWindow(QueuedWindow window)
	{
		_window = window;
	}

	@Override
	public void updateWindow()
	{
		GLFW.glfwWaitEventsTimeout(0.001f);
	}
}
