package net.whg.frameworks.external;

import static org.lwjgl.bgfx.BGFX.BGFX_RESET_VSYNC;
import static org.lwjgl.bgfx.BGFX.bgfx_init;
import static org.lwjgl.bgfx.BGFX.bgfx_init_ctor;
import static org.lwjgl.bgfx.BGFX.bgfx_shutdown;
import static org.lwjgl.glfw.GLFW.GLFW_CLIENT_API;
import static org.lwjgl.glfw.GLFW.GLFW_COCOA_RETINA_FRAMEBUFFER;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_NO_API;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.bgfx.BGFXInit;
import org.lwjgl.glfw.GLFWNativeCocoa;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.glfw.GLFWNativeX11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import net.whg.frameworks.render.Window;
import net.whg.frameworks.render.WindowProperties;

public class BGFXWindow implements Window
{
	private WindowProperties properties;
	private boolean open;
	private long window;

	@Override
	public void init(WindowProperties properties)
	{
		this.properties = properties.copy();
	}

	@Override
	public void show()
	{
		open = true;

		if (!glfwInit())
			throw new RuntimeException("Failed to initialize GLFW!");

		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);

		if (Platform.get() == Platform.MACOSX)
			glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);

		window = glfwCreateWindow(properties.width, properties.height, properties.title, 0, 0);
		if (window == NULL)
			throw new RuntimeException("Error creating GLFW window");

		glfwSetKeyCallback(window, (windowHnd, key, scancode, action, mods) ->
		{
			if (action != GLFW_RELEASE)
				return;

			switch (key) {
				case GLFW_KEY_ESCAPE:
					glfwSetWindowShouldClose(windowHnd, true);
					break;
			}
		});

		try (MemoryStack stack = stackPush())
		{
			BGFXInit init = BGFXInit.mallocStack(stack);
			bgfx_init_ctor(init);
			init.resolution(it -> it.width(properties.width).height(properties.height).reset(BGFX_RESET_VSYNC));

			switch (Platform.get()) {
				case LINUX:
					init.platformData().ndt(GLFWNativeX11.glfwGetX11Display())
							.nwh(GLFWNativeX11.glfwGetX11Window(window));
					break;
				case MACOSX:
					init.platformData().nwh(GLFWNativeCocoa.glfwGetCocoaWindow(window));
					break;
				case WINDOWS:
					init.platformData().nwh(GLFWNativeWin32.glfwGetWin32Window(window));
					break;
			}

			if (!bgfx_init(init))
			{
				throw new RuntimeException("Error initializing bgfx renderer");
			}
		}

		new BGFXRenderLoop(this).loop();

		bgfx_shutdown();
		glfwDestroyWindow(window);
		glfwTerminate();

		open = false;
	}

	@Override
	public void rebuild(WindowProperties properties)
	{
		destory();
		init(properties);
		show();
	}

	@Override
	public void destory()
	{
		if (!open)
			return;

		glfwSetWindowShouldClose(window, true);
	}

	@Override
	public boolean isOpen()
	{
		return open;
	}

	WindowProperties getWindowProperties()
	{
		return properties;
	}

	long getWindowId()
	{
		return window;
	}
}
