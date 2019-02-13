package net.whg.we.test;

import org.joml.Vector3f;
import net.whg.we.main.Plugin;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.ResourceFetcher;
import net.whg.we.resources.ResourceManager;
import net.whg.we.scene.Collision;
import net.whg.we.scene.Scene;
import net.whg.we.scene.UpdateListener;
import net.whg.we.scene.WindowedGameLoop;
import net.whg.we.ui.font.Font;
import net.whg.we.ui.terminal.Terminal;
import net.whg.we.utils.Color;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Screen;
import net.whg.we.utils.logging.Log;
import net.whg.we.utils.logging.TextBuilder;

public class TestScene implements UpdateListener
{
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private WindowedGameLoop _gameLoop;
	private Scene _scene;

	public TestScene(WindowedGameLoop gameLoop)
	{
		_gameLoop = gameLoop;
		_gameLoop.getUpdateEvent().addListener(this);
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void init()
	{
		try
		{
			Graphics graphics = _gameLoop.getGraphicsPipeline().getGraphics();
			graphics.setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));
			_scene = new Scene();

			{
				Plugin plugin = new Plugin()
				{

					@Override
					public String getPluginName()
					{
						return "TestPlugin";
					}

					@Override
					public void initPlugin()
					{
					}

					@Override
					public void enablePlugin()
					{
					}

					@Override
					public int getPriority()
					{
						return 0;
					}

				};

				ResourceManager resourceManager = _gameLoop.getResourceManager();
				ResourceFetcher fetch = new ResourceFetcher(resourceManager, graphics);

				// ModelResource terrain = (ModelResource) resourceManager.loadResource(plugin,
				// "models/terrain.model");
				// terrain.compile(_gameLoop.getGraphicsPipeline().getGraphics());
				//
				// Model model = terrain.getData();
				// model.getLocation().setScale(new Vector3f(100f, 100f, 100f));
				// model.getLocation()
				// .setRotation(new Quaternionf().rotateX((float) Math.toRadians(-90f)));
				//
				// GameObject go = _scene.getGameObjectManager().createNew();
				// go.addBehaviour(new RenderBehaviour(model));
				// go.addBehaviour(new MeshColliderBehaviour(
				// terrain.getMeshResource(0).getVertexData(), model.getLocation()));

				{
					Font font = fetch.getFont(plugin, "ui/fonts/ubuntu.fnt");
					Material mat = fetch.getMaterial(plugin, "ui/fonts/ubuntu.material");
					TextBuilder textBuilder = new TextBuilder(mat, font, graphics,
							resourceManager.getResourceDatabase());
					Terminal terminal = new Terminal(textBuilder, fetch);
					_scene.getUIStack().addComponent(terminal);
				}
			}

			_camera = new Camera();
			_scene.getRenderPass().setCamera(_camera);
			_firstPerson = new FirstPersonCamera(_camera);
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to initialize scene!", exception);
			_gameLoop.requestClose();
		}
	}

	@Override
	public void update()
	{
		try
		{
			_scene.update();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to update scene!", exception);
			_gameLoop.requestClose();
		}
	}

	@Override
	public void updateFrame()
	{
		try
		{
			_scene.updateFrame();

			_firstPerson.setMoveSpeed(Input.isKeyHeld("control") ? 70f : 7f);
			_firstPerson.update();

			Collision col = _scene.getPhysicsWorld().raycast(
					_firstPerson.getLocation().getPosition(), new Vector3f(0f, -1f, 0f), 10f);
			if (col != null)
				_firstPerson.getLocation().setPosition(col.getPosition().add(0f, 1.8f, 0f));

			if (Input.isKeyDown("q"))
				Screen.setMouseLocked(!Screen.isMouseLocked());
			if (Input.isKeyDown("escape"))
				_gameLoop.requestClose();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to update scene!", exception);
			_gameLoop.requestClose();
		}
	}

	@Override
	public void render()
	{
		try
		{
			Graphics graphics = _gameLoop.getGraphicsPipeline().getGraphics();
			graphics.clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			_scene.render();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to render scene!", exception);
			_gameLoop.requestClose();
		}
	}

	@Override
	public void dispose()
	{
		try
		{
			_scene.dispose();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to dispose scene!", exception);
			_gameLoop.requestClose();
		}
	}
}
