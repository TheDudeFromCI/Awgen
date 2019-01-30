package net.whg.we.test;

import org.joml.Math;
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
import net.whg.we.utils.Time;

public class TestScene implements UpdateListener
{
	private Model _monkeyModel;
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
				// SceneLoader loader = new SceneLoader(resourceLoader);
				// FileDatabase fileDatabase = resourceLoader.getFileDatabase();

				// _monkeyModel = loader.loadModel(fileDatabase.getResourceFile(dummyPlugin,
				// "monkey_head.fbx"), graphics);
				// Model floor = loader.loadModel(fileDatabase.getResourceFile(dummyPlugin,
				// "floor.obj"), graphics);
				// Model human = loader.loadModel(fileDatabase.getResourceFile(dummyPlugin,
				// "BaseHuman.fbx"), graphics);
				//
				// _monkeyModel.getLocation()
				// .setRotation(new Quaternionf().rotateX((float) (-Math.PI / 2f)));
				// _monkeyModel.getLocation().setScale(new Vector3f(0.25f, 0.25f, 0.25f));
				// human.getLocation().setPosition(new Vector3f(0f, 0f, -5f));
				//
				// _renderPass.addModel(_monkeyModel);
				// _renderPass.addModel(floor);
				// _renderPass.addModel(human);

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

				ModelResource modelResource =
						(ModelResource) resourceManager.loadResource(plugin, "models/human.model");
				modelResource.compile(_gameLoop.getGraphicsPipeline().getGraphics());
				_renderPass.addModel(modelResource.getData());
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
			_firstPerson.update();

			if (Input.isKeyDown("q"))
				Screen.setMouseLocked(!Screen.isMouseLocked());
			if (Input.isKeyDown("escape"))
				_gameLoop.requestClose();

			if (_monkeyModel != null)
			{
				float y = (float) (Math.sin(Time.time()) + 1f);
				_monkeyModel.getLocation().setPosition(new Vector3f(0f, y, 0f));
			}

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
