package whg.test;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.event.EventManager;
import net.whg.we.main.Plugin;
import net.whg.we.main.PluginLoader;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.RenderGroup;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.rendering.Shader;
import net.whg.we.rendering.ShaderDatabase;
import net.whg.we.rendering.Texture;
import net.whg.we.rendering.TextureProperties;
import net.whg.we.rendering.opengl.OpenGLGraphics;
import net.whg.we.resources.MeshSceneResource;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ShaderResource;
import net.whg.we.scene.Model;
import net.whg.we.utils.Color;
import net.whg.we.utils.FileUtils;
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
	private Shader _defaultShader;
	private Material _defaultMaterial;
	private Model _monkeyModel;
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private RenderGroup _renderGroup;
	private Texture _texture;

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

			((OpenGLGraphics) _core.getGraphics()).checkForErrors("Pre-Load Shader");

			File shaderFile = FileUtils.getResource(this, "normal_shader.glsl");
			ShaderResource shaderResource =
					(ShaderResource) ResourceLoader.loadResource(shaderFile);
			shaderResource.compileShader();
			_defaultShader = shaderResource.getData();
			_defaultMaterial = new Material(_defaultShader);

			((OpenGLGraphics) _core.getGraphics()).checkForErrors("Compiled Shader");

			ShaderDatabase.bindShader(_defaultShader);
			_defaultShader.loadUniform("_diffuse");
			_defaultShader.setUniformInt("_diffuse", 0);

			((OpenGLGraphics) _core.getGraphics()).checkForErrors("Loaded shader");

			{
				TextureProperties properties = new TextureProperties();
				BufferedImage image = ImageIO.read(
						FileUtils.getResource(this, "textures/male_casualsuit06_diffuse.png"));

				Color[] pixels = new Color[image.getWidth() * image.getHeight()];
				int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(),
						new int[pixels.length], 0, image.getWidth());

				int index;
				float r, g, b, a;
				for (int y = 0; y < image.getHeight(); y++)
				{
					for (int x = 0; x < image.getWidth(); x++)
					{
						index = y * image.getWidth() + x;

						r = (rgb[index] >> 16) / 255f;
						g = (rgb[index] >> 8) / 255f;
						b = rgb[index] / 255f;
						a = (rgb[index] >> 24) / 255f;
						pixels[index] = new Color(r, g, b, a);
					}
				}

				properties.setPixels(pixels, image.getWidth(), image.getHeight());

				_texture = new Texture(_core.getGraphics().prepareTexture(properties), properties);
			}

			_renderGroup = new RenderGroup();

			{
				MeshSceneResource monkeyResource = (MeshSceneResource) ResourceLoader
						.loadResource(FileUtils.getResource(this, "monkey_head.fbx"));
				monkeyResource.compile(_core.getGraphics());
				_monkeyModel = new Model(monkeyResource.getData()._meshes.get(0), _defaultMaterial);
				_monkeyModel.getLocation().setRotation(new Quaternionf().rotateX(-3.1415f / 2f));
				_renderGroup.addRenderable(_monkeyModel);

				MeshSceneResource floorResource = (MeshSceneResource) ResourceLoader
						.loadResource(FileUtils.getResource(this, "floor.obj"));
				floorResource.compile(_core.getGraphics());
				Model floorModel =
						new Model(floorResource.getData()._meshes.get(0), _defaultMaterial);
				_renderGroup.addRenderable(floorModel);

				MeshSceneResource humanResource = (MeshSceneResource) ResourceLoader
						.loadResource(FileUtils.getResource(this, "BaseHuman.fbx"));
				humanResource.compile(_core.getGraphics());

				for (int i = 0; i < humanResource.getData()._meshes.size(); i++)
				{
					Model humanModel =
							new Model(humanResource.getData()._meshes.get(i), _defaultMaterial);
					humanModel.getLocation().setPosition(new Vector3f(0f, 0f, -5f));
					humanModel.getLocation().setScale(new Vector3f(1f, 1f, 1f));
					_renderGroup.addRenderable(humanModel);
				}
			}

			_camera = new Camera();
			_firstPerson = new FirstPersonCamera(_camera);
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
			_texture.bind(0);
			_renderGroup.render(_camera);
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
		_renderGroup.forEach(r -> ((Model) r).getMesh().dispose());
		_texture.dispose();
		_defaultShader.dispose();
	}
}
