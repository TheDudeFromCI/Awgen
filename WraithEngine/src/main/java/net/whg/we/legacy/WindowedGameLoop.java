package net.whg.we.legacy;

import org.lwjgl.opengl.GL11;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.scene.Transform3D;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.ScreenClearType;
import net.whg.we.client_logic.resources.ResourceFetcher;
import net.whg.we.client_logic.resources.ResourceManager;
import net.whg.we.client_logic.resources.scene.ModelResource;
import net.whg.we.client_logic.ui.UIStack;
import net.whg.we.client_logic.ui.terminal.Terminal;
import net.whg.we.client_logic.utils.FPSLogger;
import net.whg.we.main.CorePlugin;
import net.whg.we.main.GameLoop;
import net.whg.we.scene.CameraNode;
import net.whg.we.scene.FirstPersonNode;
import net.whg.we.scene.ModelNode;

public class WindowedGameLoop implements GameLoop
{
	private boolean _isRunning;
	private ClientGameState _gameState;
	private Terminal _terminal;
	private CorePlugin _corePlugin;
	private UIStack _uiStack;

	public WindowedGameLoop(ClientGameState gameState)
	{
		_gameState = gameState;
		_corePlugin = new CorePlugin();
	}

	@Override
	public void run()
	{
		if (_isRunning)
		{
			Log.warn("Cannot run game loop! Already running!");
			return;
		}

		_isRunning = true;

		try
		{
			ResourceManager resourceManager = _gameState.getResourceManager();
			Graphics graphics = _gameState.getGraphicsPipeline().getGraphics();

			ResourceFetcher fetch = new ResourceFetcher(resourceManager, graphics);

			_uiStack = new UIStack();
			_terminal = new Terminal(fetch, _gameState);
			_uiStack.addComponent(_terminal);

			graphics.setClearScreenColor(new Color(0.2f, 0.4f, 0.8f));

			long startTime = System.currentTimeMillis();
			long usedPhysicsFrames = 0;

			SceneNode sceneRoot =
					_gameState.getSceneManager().getSceneList().getLoadedScene(0).getRoot();

			// Setup First Person Camera Nodes
			CameraNode camera = new CameraNode();
			FirstPersonNode firstPerson = new FirstPersonNode();
			camera.setParent(firstPerson);
			firstPerson.setParent(sceneRoot);

			// TODO Remove this. Loading a resources should occur through
			// commands and startup files, not pre-loaded.
			{
				ModelResource terrain = (ModelResource) resourceManager.loadResource(_corePlugin,
						"models/terrain.model");
				terrain.compile(graphics);
				Model model = terrain.getData();

				for (int i = 0; i < model.getSubMeshCount(); i++)
				{
					SubMesh sm = model.getSubMesh(i);
					ModelNode node = new ModelNode(sm.getMesh(), sm.getMaterial(), camera);

					((Transform3D) node.getTransform()).setSize(100f);
					((Transform3D) node.getTransform()).getRotation()
							.rotateX((float) Math.toRadians(-90f));

					sceneRoot.addChild(node);
				}
			}

			_gameState.getNetworkHandler().login();

			while (true)
			{
				try
				{
					if (!_gameState.getNetworkHandler().isRunning())
					{
						Log.info("Server connection closed! Shutting down.");
						requestClose();
						break;
					}

					long currentTime = System.currentTimeMillis();
					double passed = (currentTime - startTime) / 1000.0;
					double physicsFrames = passed * Time.getPhysicsFramerate();

					while (usedPhysicsFrames < physicsFrames)
					{
						usedPhysicsFrames++;

						if (!_gameState.getNetworkHandler().isRunning())
							requestClose();

						_gameState.getNetworkHandler().processPackets(_gameState);
						_gameState.getSceneManager().updateFrame();
						_uiStack.update();
					}

					// Calculate frame data
					Time.updateTime();
					FPSLogger.logFramerate();

					_gameState.getSceneManager().updateFrame();
					_uiStack.updateFrame();

					_gameState.getGraphicsPipeline().getGraphics()
							.clearScreenPass(ScreenClearType.CLEAR_COLOR_AND_DEPTH);

					GL11.glEnable(GL11.GL_DEPTH_TEST);
					_gameState.getSceneManager().render();
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					_uiStack.render();

					// End frame
					Input.endFrame();
					if (_gameState.getGraphicsPipeline().getWindow().endFrame())
						break;
				}
				catch (Exception exception)
				{
					Log.fatalf("Fatal error thrown in main thread!", exception);
					requestClose();
					break;
				}
			}
		}
		catch (Exception exception)
		{
			Log.fatalf("Uncaught exception in game loop!", exception);
		}
		finally
		{
			_gameState.getNetworkHandler().stopClient();
			_gameState.getResourceManager().disposeAllResources();
			_gameState.getGraphicsPipeline().dispose();
			_isRunning = false;
		}
	}

	@Override
	public void requestClose()
	{
		_gameState.getGraphicsPipeline().requestClose();
	}

	public Terminal getTerminal()
	{
		return _terminal;
	}

	public CorePlugin getCorePlugin()
	{
		return _corePlugin;
	}
}
