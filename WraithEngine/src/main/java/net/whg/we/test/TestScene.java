package net.whg.we.test;

import org.joml.Vector3f;
import net.whg.we.main.Plugin;
import net.whg.we.rendering.Camera;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.ScreenClearType;
import net.whg.we.resources.ResourceManager;
import net.whg.we.resources.graphics.MeshResource;
import net.whg.we.resources.scene.FontResource;
import net.whg.we.resources.scene.MaterialResource;
import net.whg.we.scene.Collision;
import net.whg.we.scene.Scene;
import net.whg.we.scene.UpdateListener;
import net.whg.we.scene.WindowedGameLoop;
import net.whg.we.ui.UIImage;
import net.whg.we.ui.UIUtils;
import net.whg.we.ui.font.Font;
import net.whg.we.utils.Color;
import net.whg.we.utils.FirstPersonCamera;
import net.whg.we.utils.Input;
import net.whg.we.utils.Screen;
import net.whg.we.utils.logging.Log;

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

	public void loadTestScene(ResourceManager resourceManager)
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
					Mesh uiMesh = new Mesh("Image Mesh", UIUtils.defaultImageVertexData(),
							_gameLoop.getGraphicsPipeline().getGraphics());
					resourceManager.getResourceDatabase()
							.addResource(new MeshResource(null, null, uiMesh));
					MaterialResource matRes = (MaterialResource) resourceManager
							.loadResource(plugin, "ui/white_ui.material");
					matRes.compile(_gameLoop.getGraphicsPipeline().getGraphics());
					Material mat = matRes.getData();
					UIImage chicken = new UIImage(uiMesh, mat);
					chicken.getPosition().set(400f, 50f);
					chicken.getSize().set(800f, 100f);
					_scene.getUIStack().addComponent(chicken);
				}

				{
					FontResource fontRes = (FontResource) resourceManager.loadResource(plugin,
							"ui/fonts/ubuntu.fnt");
					Font font = fontRes.getData();

					MaterialResource matRes = (MaterialResource) resourceManager
							.loadResource(plugin, "ui/fonts/ubuntu.material");
					matRes.compile(_gameLoop.getGraphicsPipeline().getGraphics());
					Material mat = matRes.getData();

					Mesh fontMesh = new Mesh("Font Mesh",
							UIUtils.textVertexData(font, "This is text. Open\n And more."),
							graphics);
					resourceManager.getResourceDatabase()
							.addResource(new MeshResource(null, null, fontMesh));

					UIImage textImage = new UIImage(fontMesh, mat);
					textImage.getPosition().set(0f, 50f);
					textImage.getSize().set(50f, 50f);
					_scene.getUIStack().addComponent(textImage);
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
	public void onUpdate()
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
	public void onUpdateFrame()
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

			Graphics graphics = _gameLoop.getGraphicsPipeline().getGraphics();
			graphics.clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);
			_scene.render();
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
