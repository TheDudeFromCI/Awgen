package net.whg.we.test;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.main.Plugin;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.scene.Model;
import net.whg.we.scene.RenderPass;
import net.whg.we.scene.SceneLoader;
import net.whg.we.utils.Color;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Screen;
import net.whg.we.utils.Time;
import net.whg.we.window.QueuedWindow;

public class TestScene
{
	private Model _monkeyModel;
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private RenderPass _renderPass = new RenderPass();

	public void loadTestScene(ResourceLoader resourceLoader, Graphics graphics, QueuedWindow window)
	{
		try
		{
			Plugin dummyPlugin = new Plugin()
			{

				@Override
				public void initPlugin()
				{
				}

				@Override
				public int getPriority()
				{
					return 0;
				}

				@Override
				public String getPluginName()
				{
					return "TestPlugin";
				}

				@Override
				public void enablePlugin()
				{
				}
			};

			graphics.setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));
			_renderPass = new RenderPass();

			{
				SceneLoader loader = new SceneLoader(resourceLoader);

				_monkeyModel = loader.loadModel(resourceLoader.getFileDatabase().getResourceFile(dummyPlugin, "monkey_head.fbx"),
						graphics);
				Model floor =
						loader.loadModel(resourceLoader.getFileDatabase().getResourceFile(dummyPlugin, "floor.obj"), graphics);
				Model human =
						loader.loadModel(resourceLoader.getFileDatabase().getResourceFile(dummyPlugin, "BaseHuman.fbx"), graphics);

				_monkeyModel.getLocation()
						.setRotation(new Quaternionf().rotateX((float) (-Math.PI / 2f)));
				_monkeyModel.getLocation().setScale(new Vector3f(0.25f, 0.25f, 0.25f));
				human.getLocation().setPosition(new Vector3f(0f, 0f, -5f));

				_renderPass.addModel(_monkeyModel);
				_renderPass.addModel(floor);
				_renderPass.addModel(human);
			}

			_camera = new Camera();
			_renderPass.setCamera(_camera);
			_firstPerson = new FirstPersonCamera(_camera);
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to initialize scene!", exception);
			window.requestClose();
		}
	}

	public void updateTestScene(Graphics graphics, QueuedWindow window)
	{
		try
		{
			_firstPerson.update();

			if (Input.isKeyDown("q"))
				Screen.setMouseLocked(!Screen.isMouseLocked());
			if (Input.isKeyDown("escape"))
				window.requestClose();

			float y = (float) (Math.sin(Time.time()) + 1f);
			_monkeyModel.getLocation().setPosition(new Vector3f(0f, y, 0f));
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to update scene!", exception);
			window.requestClose();
		}
	}

	public void renderTestScene(Graphics graphics, QueuedWindow window)
	{
		try
		{
			graphics.clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			_renderPass.render();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to render scene!", exception);
			window.requestClose();
		}
	}
}
