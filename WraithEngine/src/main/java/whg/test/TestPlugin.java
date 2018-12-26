package whg.test;

import java.io.File;
import org.joml.Quaternionf;
import net.whg.we.event.EventManager;
import net.whg.we.main.Plugin;
import net.whg.we.main.PluginLoader;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.RenderGroup;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.rendering.Shader;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ShaderResource;
import net.whg.we.scene.Model;
import net.whg.we.utils.Color;
import net.whg.we.utils.FileUtils;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.ModelLoader;
import net.whg.we.utils.Screen;
import whg.core.CorePlugin;
import whg.core.RenderingEventCaller.RenderingListener;

public class TestPlugin implements Plugin, RenderingListener
{
	private CorePlugin _core;
	private Shader _defaultShader;
	private Material _defaultMaterial;
	private Model _monkeyModel;
	private Model _floorModel;
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private RenderGroup _renderGroup;

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
		_core.getGraphics().setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));

		File shaderFile = FileUtils.getResource(this, "normal_shader.glsl");
		ShaderResource shaderResource = (ShaderResource) ResourceLoader.loadResource(shaderFile);
		shaderResource.compileShader();
		_defaultShader = shaderResource.getData();
		_defaultMaterial = new Material(_defaultShader);

		_renderGroup = new RenderGroup();

		{
			_monkeyModel =
					new Model(ModelLoader.loadModel(FileUtils.getResource(_core, "monkey_head.fbx"),
							_core.getGraphics())._meshes.get(0), _defaultMaterial);
			_monkeyModel.getLocation().setRotation(new Quaternionf().rotateX(-3.1415f / 2f));
			_renderGroup.addRenderable(_monkeyModel);

			_floorModel = new Model(ModelLoader.loadModel(FileUtils.getResource(_core, "floor.obj"),
					_core.getGraphics())._meshes.get(0), _defaultMaterial);
			_renderGroup.addRenderable(_floorModel);
		}

		_camera = new Camera();
		_firstPerson = new FirstPersonCamera(_camera);
	}

	@Override
	public void onPrepareRender()
	{
	}

	@Override
	public void onCullScene()
	{
	}

	@Override
	public void onFinalPrepareRender()
	{
		_firstPerson.update();

		if (Input.isKeyDown("q"))
			Screen.setMouseLocked(!Screen.isMouseLocked());
		if (Input.isKeyDown("escape"))
			_core.getWindow().requestClose();
	}

	@Override
	public void onRender()
	{
		try
		{
			_core.getGraphics().clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			_renderGroup.render(_camera);
		}
		catch (Exception exception)
		{
			_core.getWindow().requestClose();
		}
	}

	@Override
	public void onGraphicsDispose()
	{
		_monkeyModel.getMesh().dispose();
		_floorModel.getMesh().dispose();
		_defaultShader.dispose();
	}
}