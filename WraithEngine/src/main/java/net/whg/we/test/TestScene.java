package net.whg.we.test;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.main.Plugin;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.ResourceManager;
import net.whg.we.resources.scene.ModelResource;
import net.whg.we.scene.Model;
import net.whg.we.scene.RenderPass;
import net.whg.we.scene.UpdateListener;
import net.whg.we.scene.WindowedGameLoop;
import net.whg.we.utils.Color;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Screen;

public class TestScene implements UpdateListener
{
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private RenderPass _renderPass = new RenderPass();
	private WindowedGameLoop _gameLoop;

	public TestScene(WindowedGameLoop gameLoop)
	{
		_gameLoop = gameLoop;
		_gameLoop.getUpdateEvent().addListener(this);
	}

	public void loadTestScene(ResourceManager resourceManager)
	{
		try
		{
			Graphics graphics = _gameLoop.getGraphicsPipeline().getGraphics();
			graphics.setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));
			_renderPass = new RenderPass();

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

				ModelResource terrain = (ModelResource) resourceManager.loadResource(plugin,
						"models/terrain.model");
				terrain.compile(_gameLoop.getGraphicsPipeline().getGraphics());

				Model m = terrain.getData();
				m.getLocation().setScale(new Vector3f(100f, 100f, 100f));
				m.getLocation()
						.setRotation(new Quaternionf().rotateX((float) Math.toRadians(-90f)));
				_renderPass.addModel(m);
			}

			_camera = new Camera();
			_renderPass.setCamera(_camera);
			_firstPerson = new FirstPersonCamera(_camera);
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to initialize scene!", exception);
			_gameLoop.requestClose();
		}
	}

	@Override
	public void onUpdate()
	{
		try
		{
			_firstPerson.setMoveSpeed(Input.isKeyHeld("control") ? 70f : 7f);
			_firstPerson.update();

			if (Input.isKeyDown("q"))
				Screen.setMouseLocked(!Screen.isMouseLocked());
			if (Input.isKeyDown("escape"))
				_gameLoop.requestClose();

			Graphics graphics = _gameLoop.getGraphicsPipeline().getGraphics();
			graphics.clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			_renderPass.render();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to update scene!", exception);
			_gameLoop.requestClose();
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
