package net.whg.we.legacy;

import java.io.File;
import net.whg.frameworks.network.multiplayer.MultiplayerClient;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.SimpleFileDatabase;
import net.whg.we.client_logic.connect.ClientPlayerList;
import net.whg.we.client_logic.rendering.GraphicsPipeline;
import net.whg.we.client_logic.resources.graphics.GLSLShaderLoader;
import net.whg.we.client_logic.resources.graphics.MeshLoader;
import net.whg.we.client_logic.resources.graphics.TextureLoader;
import net.whg.we.client_logic.resources.scene.FontLoader;
import net.whg.we.client_logic.resources.scene.MaterialLoader;
import net.whg.we.client_logic.resources.scene.ModelLoader;
import net.whg.we.main.GameState;
import net.whg.we.scene.SceneListManager;

public class ClientGameState implements GameState
{
	private static ResourceManager buildResourceManager()
	{
		File baseFolder = new File(System.getProperty("user.dir"));
		FileDatabase fileDatabase = new SimpleFileDatabase(baseFolder);
		ResourceDatabase resourceDatabase = new ResourceDatabase();
		ResourceLoader resourceLoader = new ResourceLoader();

		resourceLoader.addFileLoader(new GLSLShaderLoader());
		resourceLoader.addFileLoader(new MeshLoader());
		resourceLoader.addFileLoader(new TextureLoader());
		resourceLoader.addFileLoader(new MaterialLoader(fileDatabase));
		resourceLoader.addFileLoader(new ModelLoader(fileDatabase));
		resourceLoader.addFileLoader(new FontLoader());

		return new ResourceManager(resourceDatabase, resourceLoader, fileDatabase);
	}

	private ResourceManager _resourceManager;
	private WindowedGameLoop _gameLoop;
	private SceneListManager _sceneManager;
	private ClientPlayerList _playerList;
	private GraphicsPipeline _graphicsPipeline;
	private MultiplayerClient _networkHandler;

	public ClientGameState(MultiplayerClient client)
	{
		_resourceManager = buildResourceManager();
		_gameLoop = new WindowedGameLoop(this);
		_sceneManager = new SceneListManager();
		_playerList = new ClientPlayerList(client.getUsername(), client.getToken());
		_graphicsPipeline = new GraphicsPipeline();
		_networkHandler = client;
	}

	@Override
	public WindowedGameLoop getGameLoop()
	{
		return _gameLoop;
	}

	@Override
	public SceneListManager getSceneManager()
	{
		return _sceneManager;
	}

	@Override
	public ClientPlayerList getPlayerList()
	{
		return _playerList;
	}

	@Override
	public ResourceManager getResourceManager()
	{
		return _resourceManager;
	}

	public GraphicsPipeline getGraphicsPipeline()
	{
		return _graphicsPipeline;
	}

	@Override
	public MultiplayerClient getNetworkHandler()
	{
		return _networkHandler;
	}
}
