package net.whg.frameworks.external;

import static org.lwjgl.bgfx.BGFX.BGFX_CLEAR_COLOR;
import static org.lwjgl.bgfx.BGFX.BGFX_CLEAR_DEPTH;
import static org.lwjgl.bgfx.BGFX.BGFX_DEBUG_TEXT;
import static org.lwjgl.bgfx.BGFX.bgfx_dbg_text_clear;
import static org.lwjgl.bgfx.BGFX.bgfx_dbg_text_image;
import static org.lwjgl.bgfx.BGFX.bgfx_dbg_text_printf;
import static org.lwjgl.bgfx.BGFX.bgfx_frame;
import static org.lwjgl.bgfx.BGFX.bgfx_get_renderer_name;
import static org.lwjgl.bgfx.BGFX.bgfx_get_renderer_type;
import static org.lwjgl.bgfx.BGFX.bgfx_set_debug;
import static org.lwjgl.bgfx.BGFX.bgfx_set_view_clear;
import static org.lwjgl.bgfx.BGFX.bgfx_set_view_rect;
import static org.lwjgl.bgfx.BGFX.bgfx_touch;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import java.nio.ByteBuffer;
import net.whg.frameworks.render.Logo;
import net.whg.frameworks.render.RenderLoop;

public class BGFXRenderLoop implements RenderLoop
{
	private BGFXWindow window;

	BGFXRenderLoop(BGFXWindow window)
	{
		this.window = window;
	}

	@Override
	public void loop()
	{
		System.out.println("bgfx renderer: " + bgfx_get_renderer_name(bgfx_get_renderer_type()));

		// Enable debug text.
		bgfx_set_debug(BGFX_DEBUG_TEXT);

		bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, 0x303030ff, 1.0f, 0);

		ByteBuffer logo = Logo.createLogo();

		while (!glfwWindowShouldClose(window.getWindowId()))
		{
			glfwPollEvents();

			// Set view 0 default viewport.
			bgfx_set_view_rect(0, 0, 0, window.getWindowProperties().width, window.getWindowProperties().height);

			// This dummy draw call is here to make sure that view 0 is cleared
			// if no other draw calls are submitted to view 0.
			bgfx_touch(0);

			// Use debug font to print information about this example.
			bgfx_dbg_text_clear(0, false);
			bgfx_dbg_text_image(Math.max(window.getWindowProperties().width / 2 / 8, 20) - 20,
					Math.max(window.getWindowProperties().height / 2 / 16, 6) - 6, 40, 12, logo, 160);
			bgfx_dbg_text_printf(0, 1, 0x1f, "bgfx/examples/25-c99");
			bgfx_dbg_text_printf(0, 2, 0x3f, "Description: Initialization and debug text with C99 API.");

			bgfx_dbg_text_printf(0, 3, 0x0f,
					"Color can be changed with ANSI \u001b[9;me\u001b[10;ms\u001b[11;mc\u001b[12;ma\u001b[13;mp\u001b[14;me\u001b[0m code too.");

			bgfx_dbg_text_printf(80, 4, 0x0f,
					"\u001b[;0m    \u001b[;1m    \u001b[; 2m    \u001b[; 3m    \u001b[; 4m    \u001b[; 5m    \u001b[; 6m    \u001b[; 7m    \u001b[0m");
			bgfx_dbg_text_printf(80, 5, 0x0f,
					"\u001b[;8m    \u001b[;9m    \u001b[;10m    \u001b[;11m    \u001b[;12m    \u001b[;13m    \u001b[;14m    \u001b[;15m    \u001b[0m");

			// Advance to next frame. Rendering thread will be kicked to
			// process submitted rendering primitives.
			bgfx_frame(false);
		}
	}
}
