package whg.WraithEngine.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import whg.WraithEngine.gamelogic.RenderLoop;

public class Window
{
	private long _windowId;
	private String _windowName = "Untitled WraithEngine Project";
	private int _windowWidth = 640;
	private int _windowHeight = 480;
	private boolean _vSync = false;
	private boolean _resizeable = false;
	private RenderLoop _renderLoop;
	private boolean _destroyed;
	private Object _lock = new Object();
	private GLFWEventQueue _eventQueue;
	private MouseMoveEvent _mouseMoveEvent;
	
	public Window()
	{
		_windowId = MemoryUtil.NULL;
		_eventQueue = new GLFWEventQueue();
		_mouseMoveEvent = new MouseMoveEvent(this);
	}
	
	public void setWindowName(String windowName)
	{
		_windowName = windowName;
	}
	
	public void setWindowSize(int width, int height)
	{
		_windowWidth = width;
		_windowHeight = height;
	}
	
	public String getWindowName()
	{
		return _windowName;
	}
	
	public void setRenderLoop(RenderLoop renderLoop)
	{
		_renderLoop = renderLoop;
	}
	
	public RenderLoop getRenderLoop()
	{
		return _renderLoop;
	}
	
	public int getWindowWidth()
	{
		return _windowWidth;
	}
	
	public int getWindowHeight()
	{
		return _windowHeight;
	}
	
	public boolean doesWindowExist()
	{
		return _windowId != MemoryUtil.NULL;
	}
	
	public void setVSync(boolean vSync)
	{
		_vSync = vSync;
	}
	
	public void setResizable(boolean resizable)
	{
		_resizeable = resizable;
	}
	
	public boolean hasVSync()
	{
		return _vSync;
	}
	
	public boolean isResizable()
	{
		return _resizeable;
	}
	
	public void loop()
	{
		if (_renderLoop == null)
			throw new IllegalStateException("RenderLoop not defined!");
		_destroyed = false;
		
		_eventQueue.clearEvents();
		_mouseMoveEvent.setMousePos(0f, 0f);
		
		// TODO Remove
		{
			GLFW.glfwSetInputMode(_windowId, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		}

		Thread t = new Thread(() -> {
			GLFW.glfwMakeContextCurrent(_windowId);

			if(_vSync)
				GLFW.glfwSwapInterval(1);
			else
				GLFW.glfwSwapInterval(0);

			GL.createCapabilities();
			_renderLoop.loop(this);
		});
		t.setName("Rendering");
		t.start();
		
		while (!GLFW.glfwWindowShouldClose(_windowId))
		{
			GLFW.glfwWaitEvents();
			synchronized (_lock)
			{
				if (_destroyed)
					GLFW.glfwSetWindowShouldClose(_windowId, true);
			}
		}
		requestClose();

		_eventQueue.clearEvents();
	}
	
	public boolean isRequestingClose()
	{
		synchronized(_lock)
		{
			return _destroyed;
		}
	}
	
	public void requestClose()
	{
		synchronized (_lock)
		{
			_destroyed = true;
		}
	}
	
	public void pollEvents()
	{
		_eventQueue.handleEvents();

		synchronized (_mouseMoveEvent)
		{
			_mouseMoveEvent.handleEvent();
		}
	}
	
	public void endFrame()
	{
		synchronized (_lock)
		{
			GLFW.glfwSwapBuffers(_windowId);
			GLFW.glfwPollEvents();
		}
	}
	
	public void build()
	{
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		
		if (!_resizeable)
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		
		_windowId = GLFW.glfwCreateWindow(_windowWidth, _windowHeight, _windowName, MemoryUtil.NULL, MemoryUtil.NULL);
		if (!doesWindowExist())
			throw new RuntimeException("Failed to create GLFW window!");
		
		assignCallbacks();
		
		try (MemoryStack stack = MemoryStack.stackPush())
		{
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			int x = (vidmode.width() - _windowWidth) / 2;
			int y = (vidmode.height() - _windowHeight) / 2;
			GLFW.glfwSetWindowPos(_windowId, x, y);
		}
				
		GLFW.glfwShowWindow(_windowId);
	}
	
	private void assignCallbacks()
	{
		GLFW.glfwSetWindowSizeCallback(_windowId, (long window, int width, int height) -> {
			_eventQueue.addEvent(new WindowResizeEvent(this, width, height));
		});

		GLFW.glfwSetKeyCallback(_windowId, (long window, int key, int scancode, int action, int mods) -> {
			_eventQueue.addEvent(new KeyPressedEvent(this, key, action, mods));
		});

		GLFW.glfwSetCursorPosCallback(_windowId, (long window, double mouseX, double mouseY) -> {
			synchronized (_mouseMoveEvent)
			{
				_mouseMoveEvent.setMousePos((float)mouseX, (float)mouseY);
			}
		});
	}
	
	public void destroy()
	{
		if (!doesWindowExist())
			return;

		synchronized (_lock)
		{
			Callbacks.glfwFreeCallbacks(_windowId);
			GLFW.glfwDestroyWindow(_windowId);
			_windowId = MemoryUtil.NULL;
		}
	}
}
