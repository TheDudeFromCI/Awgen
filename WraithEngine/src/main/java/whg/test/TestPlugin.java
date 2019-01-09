package whg.test;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.event.EventManager;
import net.whg.we.main.Plugin;
import net.whg.we.main.PluginLoader;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.scene.Model;
import net.whg.we.scene.RenderPass;
import net.whg.we.scene.SceneUtils;
import net.whg.we.utils.Color;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Screen;
import net.whg.we.utils.Time;
import whg.core.CorePlugin;
import whg.core.RenderingEventCaller.RenderingListener;

public class TestPlugin implements Plugin, RenderingListener
{
	private CorePlugin _core;
	private Model _monkeyModel;
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private RenderPass _renderPass = new RenderPass();

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
		_core = (CorePlugin) PluginLoader.GetPlugin("Core");
		EventManager.registerListener(_core, CorePlugin.RENDERING_EVENTCALLER, this);
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public void onGraphicsInit()
	{
		try
		{
			_core.getGraphics().setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));

			{
				_monkeyModel = SceneUtils.loadModel(this, "monkey_head.fbx", _core.getGraphics());
				Model floor = SceneUtils.loadModel(this, "floor.obj", _core.getGraphics());
				Model human = SceneUtils.loadModel(this, "BaseHuman.fbx", _core.getGraphics());

				_monkeyModel.getLocation()
						.setRotation(new Quaternionf().rotateX((float) (-Math.PI / 2f)));
				human.getLocation().setPosition(new Vector3f(0f, 0f, -5f));

				_renderPass.addModel(_monkeyModel);
				_renderPass.addModel(floor);
				_renderPass.addModel(human);
			}

			_camera = new Camera();
			_firstPerson = new FirstPersonCamera(_camera);
			_renderPass.setCamera(_camera);
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to initialize scene!", exception);
			_core.getWindow().requestClose();
		}
	}

	@Override
	public void onPrepareRender()
	{
		float y = (float) (Math.sin(Time.time()) + 1f);
		_monkeyModel.getLocation().setPosition(new Vector3f(0f, y, 0f));
	}

	@Override
	public void onCullScene()
	{
	}

	@Override
	public void onFinalPrepareRender()
	{
		try
		{
			_firstPerson.update();

			if (Input.isKeyDown("q"))
				Screen.setMouseLocked(!Screen.isMouseLocked());
			if (Input.isKeyDown("escape"))
				_core.getWindow().requestClose();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to render scene!", exception);
			_core.getWindow().requestClose();
		}
	}

	@Override
	public void onRender()
	{
		try
		{
			_core.getGraphics().clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			_renderPass.render();
		}
		catch (Exception exception)
		{
			Log.fatalf("Failed to render scene!", exception);
			_core.getWindow().requestClose();
		}
	}

	@Override
	public void onGraphicsDispose()
	{
		ResourceDatabase.disposeAll();
	}
}
