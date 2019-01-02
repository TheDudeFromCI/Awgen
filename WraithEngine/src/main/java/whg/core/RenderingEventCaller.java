package whg.core;

import net.whg.we.event.EventCallerBase;
import net.whg.we.event.Listener;
import net.whg.we.main.Plugin;
import whg.core.RenderingEventCaller.RenderingListener;

/**
 * Represents an event caller for handling rendering events.
 *
 * @author TheDudeFromCI
 */
public class RenderingEventCaller extends EventCallerBase<RenderingListener>
{
	private static final int ON_GRAPHICS_INIT_EVENT = 0;
	private static final int ON_PREPARE_RENDER_EVENT = 1;
	private static final int ON_CULL_SCENE_EVENT = 2;
	private static final int ON_FINAL_PREPARE_RENDER_EVENT = 3;
	private static final int ON_RENDER_EVENT = 4;
	private static final int ON_GRAPHICS_DISPOSE_EVENT = 5;

	/**
	 * Handles rendering events.
	 *
	 * @author TheDudeFromCI
	 */
	public static interface RenderingListener extends Listener
	{
		/**
		 * Called once at the start of the graphics game loop. This event is used for
		 * setting one time graphics settings, or loading scene information.
		 */
		public void onGraphicsInit();

		/**
		 * Called when at the start of a graphics loop to prepare a render. This is
		 * generally for assigning object positions and rotations based on physics
		 * information.
		 */
		public void onPrepareRender();

		/**
		 * Called after {@link #onPrepareRender()}. This event is used for culling
		 * objects and determining what objects should and should not be rendered.
		 */
		public void onCullScene();

		/**
		 * Called after {@link #onCullScene()}. This event is used for final tweaks to
		 * objects which should be rendered, such as loading animations, or updating the
		 * camera.
		 */
		public void onFinalPrepareRender();

		/**
		 * Called after {@link #onFinalPrepareRender()}. This event is used for
		 * rendering the scene.
		 */
		public void onRender();

		/**
		 * Called once after the graphics game loop has ended and is used for disposing
		 * all graphics and rendering data being used.
		 */
		public void onGraphicsDispose();
	}

	private CorePlugin _core;

	RenderingEventCaller(CorePlugin core)
	{
		_core = core;
	}

	@Override
	public String getName()
	{
		return CorePlugin.RENDERING_EVENTCALLER;
	}

	@Override
	public Plugin getPlugin()
	{
		return _core;
	}

	void onGraphicsInit()
	{
		callEvent(ON_GRAPHICS_INIT_EVENT);
	}

	void onPrepareRender()
	{
		callEvent(ON_PREPARE_RENDER_EVENT);
	}

	void onCullScene()
	{
		callEvent(ON_CULL_SCENE_EVENT);
	}

	void onFinalPrepareRender()
	{
		callEvent(ON_FINAL_PREPARE_RENDER_EVENT);
	}

	void onRender()
	{
		callEvent(ON_RENDER_EVENT);
	}

	void onGraphicsDispose()
	{
		callEvent(ON_GRAPHICS_DISPOSE_EVENT);
	}

	@Override
	protected void runEvent(RenderingListener t, int index, Object[] args)
	{
		switch (index)
		{
			case ON_GRAPHICS_INIT_EVENT:
				t.onGraphicsInit();
				break;

			case ON_PREPARE_RENDER_EVENT:
				t.onPrepareRender();
				break;

			case ON_CULL_SCENE_EVENT:
				t.onCullScene();
				break;

			case ON_FINAL_PREPARE_RENDER_EVENT:
				t.onFinalPrepareRender();
				break;

			case ON_RENDER_EVENT:
				t.onRender();
				break;

			case ON_GRAPHICS_DISPOSE_EVENT:
				t.onGraphicsDispose();
				break;

			default:
				throw new RuntimeException("Unknown event!");
		}
	}
}
