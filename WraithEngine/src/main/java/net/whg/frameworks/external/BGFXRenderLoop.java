package net.whg.frameworks.external;

import static org.lwjgl.bgfx.BGFX.BGFX_CLEAR_COLOR;
import static org.lwjgl.bgfx.BGFX.BGFX_CLEAR_DEPTH;
import static org.lwjgl.bgfx.BGFX.BGFX_INVALID_HANDLE;
import static org.lwjgl.bgfx.BGFX.BGFX_STATE_DEFAULT;
import static org.lwjgl.bgfx.BGFX.bgfx_create_program;
import static org.lwjgl.bgfx.BGFX.bgfx_destroy_index_buffer;
import static org.lwjgl.bgfx.BGFX.bgfx_destroy_program;
import static org.lwjgl.bgfx.BGFX.bgfx_destroy_vertex_buffer;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_begin;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_end;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_set_index_buffer;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_set_state;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_set_transform;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_set_vertex_buffer;
import static org.lwjgl.bgfx.BGFX.bgfx_encoder_submit;
import static org.lwjgl.bgfx.BGFX.bgfx_frame;
import static org.lwjgl.bgfx.BGFX.bgfx_set_view_clear;
import static org.lwjgl.bgfx.BGFX.bgfx_set_view_rect;
import static org.lwjgl.bgfx.BGFX.bgfx_set_view_transform;
import static org.lwjgl.bgfx.BGFX.bgfx_touch;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.bgfx.BGFX;
import org.lwjgl.bgfx.BGFXVertexDecl;
import org.lwjgl.system.MemoryUtil;
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
		System.out.println("Loop1");

		bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, 0x303030ff, 1.0f, 0);

		try
		{
			create();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		System.out.println("Loop2");

		long time = System.currentTimeMillis();
		while (!glfwWindowShouldClose(window.getWindowId()))
		{
			System.out.println("Loop3");
			glfwPollEvents();
			bgfx_set_view_rect(0, 0, 0, window.getWindowProperties().width, window.getWindowProperties().height);

			System.out.println("Loop4");
			bgfx_touch(0);
			render((float) ((System.currentTimeMillis() - time) / 1000.0));

			System.out.println("Loop5");
			bgfx_frame(false);
		}

		System.out.println("Loop6");
		dispose();
	}

	private static final Object[][] cubeVertices =
	{
		{
			-1.0f, 1.0f, 1.0f, 0xff000000
		},
		{
			1.0f, 1.0f, 1.0f, 0xff0000ff
		},
		{
			-1.0f, -1.0f, 1.0f, 0xff00ff00
		},
		{
			1.0f, -1.0f, 1.0f, 0xff00ffff
		},
		{
			-1.0f, 1.0f, -1.0f, 0xffff0000
		},
		{
			1.0f, 1.0f, -1.0f, 0xffff00ff
		},
		{
			-1.0f, -1.0f, -1.0f, 0xffffff00
		},
		{
			1.0f, -1.0f, -1.0f, 0xffffffff
		}
	};

	private static final int[] cubeIndices =
	{
		0, 1, 2, // 0
		1, 3, 2, 4, 6, 5, // 2
		5, 6, 7, 0, 2, 4, // 4
		4, 2, 6, 1, 5, 3, // 6
		5, 7, 3, 0, 4, 1, // 8
		4, 5, 1, 2, 3, 6, // 10
		6, 3, 7
	};

	private BGFXVertexDecl decl;
	private ByteBuffer vertices;
	private short vbh;
	private ByteBuffer indices;
	private short ibh;
	private short program;

	private Matrix4f view = new Matrix4f();
	private FloatBuffer viewBuf;
	private Matrix4f proj = new Matrix4f();
	private FloatBuffer projBuf;
	private Matrix4f model = new Matrix4f();
	private FloatBuffer modelBuf;

	private void create() throws IOException
	{
		BGFXDemoUtil.configure(BGFX.BGFX_RENDERER_TYPE_OPENGL);
		decl = BGFXDemoUtil.createVertexDecl(false, true, 0);

		vertices = MemoryUtil.memAlloc(8 * (3 * 4 + 4));

		vbh = BGFXDemoUtil.createVertexBuffer(vertices, decl, cubeVertices);

		indices = MemoryUtil.memAlloc(cubeIndices.length * 2);

		ibh = BGFXDemoUtil.createIndexBuffer(indices, cubeIndices);

		short vs = BGFXDemoUtil.loadShader("vs_cubes");
		short fs = BGFXDemoUtil.loadShader("fs_cubes");

		program = bgfx_create_program(vs, fs, true);

		viewBuf = MemoryUtil.memAllocFloat(16);
		projBuf = MemoryUtil.memAllocFloat(16);
		modelBuf = MemoryUtil.memAllocFloat(16);
	}

	private void render(float time)
	{
		BGFXDemoUtil.lookAt(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, -35.0f), view);
		BGFXDemoUtil.perspective(60.0f, window.getWindowProperties().width, window.getWindowProperties().height, 0.1f,
				100.0f, proj);

		view.get(viewBuf);
		proj.get(projBuf);

		bgfx_set_view_transform(0, viewBuf, projBuf);

		long encoder = bgfx_encoder_begin(false);
		for (int yy = 0; yy < 11; ++yy)
		{
			for (int xx = 0; xx < 11; ++xx)
			{
				model.identity().translate(-15.0f + xx * 3.0f, -15.0f + yy * 3.0f, 0.0f)
						.rotateAffineXYZ(time + xx * 0.21f, time + yy * 0.37f, 0.0f);

				model.get(modelBuf);

				bgfx_encoder_set_transform(encoder, modelBuf);

				bgfx_encoder_set_vertex_buffer(encoder, 0, vbh, 0, 8, BGFX_INVALID_HANDLE);
				bgfx_encoder_set_index_buffer(encoder, ibh, 0, 36);

				bgfx_encoder_set_state(encoder, BGFX_STATE_DEFAULT, 0);

				bgfx_encoder_submit(encoder, 0, program, 0, false);
			}
		}
		bgfx_encoder_end(encoder);
	}

	private void dispose()
	{
		MemoryUtil.memFree(viewBuf);
		MemoryUtil.memFree(projBuf);
		MemoryUtil.memFree(modelBuf);

		bgfx_destroy_program(program);

		bgfx_destroy_index_buffer(ibh);
		MemoryUtil.memFree(indices);

		bgfx_destroy_vertex_buffer(vbh);
		MemoryUtil.memFree(vertices);

		decl.free();
	}
}
