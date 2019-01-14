package net.whg.we.main;

import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.Version;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.GraphicsFactory;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.GLSLShaderLoader;
import net.whg.we.resources.MeshSceneLoader;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.TextureLoader;
import net.whg.we.scene.Model;
import net.whg.we.scene.RenderPass;
import net.whg.we.scene.SceneUtils;
import net.whg.we.utils.Color;
import net.whg.we.utils.DefaultWindowListener;
import net.whg.we.utils.FPSLogger;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Log;
import net.whg.we.utils.Screen;
import net.whg.we.utils.Time;
import net.whg.we.window.QueuedWindow;
import net.whg.we.window.WindowBuilder;

/**
 * The program entry class. This class is used for the purpose of initializing
 * the plugin loader, all core plugins, and hunting down and launching local
 * plugins.
 *
 * @author TheDudeFromCI
 */
public class WraithEngine
{
	/**
	 * The current version of WraithEngine being run.
	 */
	public static final String VERSION = "v0.1.0a";

	/**
	 * The program entry method.
	 *
	 * @param args
	 *            - Arguments from command line. Used for determining program
	 *            functions. Currently, there are no extra runtime flags that can be
	 *            used at this time.
	 */
	public static void main(String[] args)
	{
		// Set default log parameters
		Log.setLogLevel(Log.TRACE);

		// Log some automatic system info
		Log.trace("Starting WraithEngine.");
		Log.debugf("WraithEngine Version: %s", VERSION);
		Log.debugf("Operating System: %s", System.getProperty("os.name"));
		Log.debugf("Operating System Arch: %s", System.getProperty("os.arch"));
		Log.debugf("Java Version: %s", System.getProperty("java.version"));
		Log.debugf("Java Vendor: %s", System.getProperty("java.vendor"));
		Log.debugf("System User: %s", System.getProperty("user.name"));
		Log.debugf("Working Directory: %s", System.getProperty("user.dir"));
		Log.debugf("LWJGL Version: %s", Version.getVersion());

		// Load core functions
		ResourceLoader.addFileLoader(new GLSLShaderLoader());
		ResourceLoader.addFileLoader(new MeshSceneLoader());
		ResourceLoader.addFileLoader(new TextureLoader());

		// Load plugins
		PluginLoader pluginLoader = new PluginLoader();
		pluginLoader.loadPluginsFromFile();

		// Enable plugins
		pluginLoader.enableAllPlugins();

		// Create main window
		Graphics graphics = GraphicsFactory.createInstance(GraphicsFactory.OPENGL_ENGINE);
		QueuedWindow window = new WindowBuilder(WindowBuilder.WINDOW_ENGINE_GLFW)
				.setName("Untitled Project").setResizable(false).setSize(640, 480).setVSync(false)
				.setListener(new DefaultWindowListener()).setGraphicsEngine(graphics).build();
		graphics.init();
		Screen.setWindow(window);

		// Start main game loop
		loadTestScene(graphics, window);
		while (true)
		{
			try
			{
				// Calculate frame data
				Time.updateTime();
				FPSLogger.logFramerate();

				updateTestScene(graphics, window);
				renderTestScene(graphics, window);

				// End frame
				Input.endFrame();
				if (window.endFrame())
					break;
			}
			catch (Exception exception)
			{
				Log.fatalf("Fatal error thrown in main thread!", exception);
				window.requestClose();
				break;
			}
		}

		// Dispose all resources
		ResourceDatabase.disposeAll();
		graphics.dispose();
	}

	private static Model _monkeyModel;
	private static FirstPersonCamera _firstPerson;
	private static Camera _camera;
	private static RenderPass _renderPass = new RenderPass();

	private static void loadTestScene(Graphics graphics, QueuedWindow window)
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
				_monkeyModel = SceneUtils
						.loadModel(new ResourceFile(dummyPlugin, "monkey_head.fbx"), graphics);
				Model floor =
						SceneUtils.loadModel(new ResourceFile(dummyPlugin, "floor.obj"), graphics);
				Model human = SceneUtils.loadModel(new ResourceFile(dummyPlugin, "BaseHuman.fbx"),
						graphics);

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

	private static void updateTestScene(Graphics graphics, QueuedWindow window)
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

	private static void renderTestScene(Graphics graphics, QueuedWindow window)
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
