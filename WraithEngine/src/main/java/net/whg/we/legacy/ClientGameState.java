package net.whg.we.legacy;

import java.io.File;
import net.whg.frameworks.network.multiplayer.MultiplayerClient;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.connect.ClientPlayerList;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.GraphicsPipeline;
import net.whg.we.main.GameState;
import net.whg.we.resource.MeshConverterLoader;
import net.whg.we.resource.MeshLoader;
import net.whg.we.resource.ShaderConverterLoader;
import net.whg.we.resource.ShaderLoader;
import net.whg.we.resource.SimpleFileDatabase;
import net.whg.we.resource.TextureConverterLoader;
import net.whg.we.resource.TextureLoader;
import net.whg.we.scene.SceneListManager;

public class ClientGameState implements GameState
{
	private static ResourceManager buildResourceManager(Graphics graphics)
	{
		File baseFolder = new File(System.getProperty("user.dir"));
		FileDatabase fileDatabase = new SimpleFileDatabase(baseFolder);
		ResourceDatabase resourceDatabase = new ResourceDatabase();
		ResourceLoader resourceLoader = new ResourceLoader();

		resourceLoader.addFileLoader(new MeshConverterLoader(graphics));
		resourceLoader.addFileLoader(new MeshLoader(graphics));
		resourceLoader.addFileLoader(new TextureConverterLoader(graphics));
		resourceLoader.addFileLoader(new TextureLoader(graphics));
		resourceLoader.addFileLoader(new ShaderConverterLoader(graphics));
		resourceLoader.addFileLoader(new ShaderLoader(graphics));

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
		_graphicsPipeline = new GraphicsPipeline();
		_resourceManager = buildResourceManager(_graphicsPipeline.getGraphics());
		_gameLoop = new WindowedGameLoop(this);
		_sceneManager = new SceneListManager();
		_playerList = new ClientPlayerList(client.getUsername(), client.getToken());
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
