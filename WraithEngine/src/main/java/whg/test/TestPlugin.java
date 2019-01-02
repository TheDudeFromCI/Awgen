package whg.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.event.EventManager;
import net.whg.we.main.Plugin;
import net.whg.we.main.PluginLoader;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.rendering.Shader;
import net.whg.we.rendering.ShaderDatabase;
import net.whg.we.rendering.Texture;
import net.whg.we.rendering.TextureProperties;
import net.whg.we.resources.DisposableResource;
import net.whg.we.resources.MeshSceneResource;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ShaderResource;
import net.whg.we.scene.Model;
import net.whg.we.scene.RenderPass;
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
	private Model _monkeyModel;
	private FirstPersonCamera _firstPerson;
	private Camera _camera;
	private ArrayList<DisposableResource> _resources = new ArrayList<>();
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

	private Shader loadShader(String name)
	{
		File shaderFile = FileUtils.getResource(this, name);
		ShaderResource shaderResource = (ShaderResource) ResourceLoader.loadResource(shaderFile);
		shaderResource.compileShader();
		Shader shader = shaderResource.getData();
		_resources.add(shader);

		return shader;
	}

	private Texture loadTexture(String name) throws IOException
	{
		TextureProperties properties = new TextureProperties();
		BufferedImage image = ImageIO.read(FileUtils.getResource(this, name));

		Color[] pixels = new Color[image.getWidth() * image.getHeight()];
		int[] rgb = image.getRGB(0, 0, image.getWidth(), image.getHeight(), new int[pixels.length],
				0, image.getWidth());

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

		Texture texture = new Texture(_core.getGraphics().prepareTexture(properties), properties);
		_resources.add(texture);
		return texture;
	}

	private Material loadMaterial(Shader shader, Texture texture)
	{
		return loadMaterial(shader, new Texture[]
		{
				texture
		});
	}

	private Material loadMaterial(Shader shader, Texture[] textures)
	{
		Material material = new Material(shader);
		material.setTextures(textures);
		return material;
	}

	private Model loadModel(String name, boolean rotate, Material mat)
	{
		MeshSceneResource scene =
				(MeshSceneResource) ResourceLoader.loadResource(FileUtils.getResource(this, name));
		scene.compile(_core.getGraphics());

		int subMeshCount = scene.getData()._meshes.size();
		Mesh[] meshes = new Mesh[subMeshCount];
		Material[] materials = new Material[subMeshCount];

		for (int i = 0; i < subMeshCount; i++)
		{
			meshes[i] = scene.getData()._meshes.get(i);
			materials[i] = mat;
			Log.trace("Loaded Mesh: " + meshes[i].getMeshName());

			_resources.add(meshes[i]);
		}

		Model model = new Model(meshes, materials);
		_renderPass.addModel(model);

		if (rotate)
			model.getLocation().setRotation(new Quaternionf().rotateX(-3.1415f / 2f));

		return model;
	}

	@Override
	public void onGraphicsInit()
	{
		try
		{
			_core.getGraphics().setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));

			Shader shader = loadShader("normal_shader.glsl");
			Texture texture = loadTexture("textures/male_casualsuit06_diffuse.png");
			Material material = loadMaterial(shader, texture);

			ShaderDatabase.bindShader(shader);
			shader.setUniformInt("_diffuse", 0);

			{
				_monkeyModel = loadModel("monkey_head.fbx", true, material);
				loadModel("floor.obj", false, material);

				Model human = loadModel("BaseHuman.fbx", false, material);
				human.getLocation().setPosition(new Vector3f(0f, 0f, -5f));
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
		for (DisposableResource res : _resources)
			res.dispose();
		_resources.clear();
	}
}
