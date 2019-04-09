package net.whg.we.legacy;

import java.io.File;
import net.whg.frameworks.command.CommandManager;
import net.whg.frameworks.network.multiplayer.MultiplayerServer;
import net.whg.frameworks.network.server.ServerPlayerList;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.SimpleFileDatabase;
import net.whg.we.commands.CommandUtils;
import net.whg.we.main.GameState;
import net.whg.we.main.PluginLoader;
import net.whg.we.scene.SceneListManager;

public class ServerGameState implements GameState
{
	private static ResourceManager buildResourceManager()
	{
		File baseFolder = new File(System.getProperty("user.dir"));
		FileDatabase fileDatabase = new SimpleFileDatabase(baseFolder);
		ResourceDatabase resourceDatabase = new ResourceDatabase();
		ResourceLoader resourceLoader = new ResourceLoader();

		// resourceLoader.addFileLoader(new GLSLShaderLoader());
		// resourceLoader.addFileLoader(new MeshLoader());
		// resourceLoader.addFileLoader(new TextureLoader());
		// resourceLoader.addFileLoader(new MaterialLoader());
		// resourceLoader.addFileLoader(new ModelLoader());
		// resourceLoader.addFileLoader(new FontLoader());

		return new ResourceManager(resourceDatabase, resourceLoader, fileDatabase);
	}

	private static ServerPlayerList buildPlayerList(MultiplayerServer server)
	{
		return new ServerPlayerList(server.getPacketManager());
	}

	private static PluginLoader buildPluginLoader(ResourceManager resourceManager)
	{
		PluginLoader pluginLoader = new PluginLoader();

		pluginLoader.loadPluginsFromFile(resourceManager.getFileDatabase());
		pluginLoader.enableAllPlugins();

		return pluginLoader;
	}

	private static CommandManager buildCommandManager(ServerGameState state)
	{
		CommandManager commandManager = new CommandManager();
		CommandUtils.addAdvancedCommands(commandManager.getCommandList(), state);

		return commandManager;
	}

	private ResourceManager _resourceManager;
	private PluginLoader _pluginLoader;
	private ServerGameLoop _gameLoop;
	private SceneListManager _sceneManager;
	private ServerPlayerList _playerList;
	private CommandManager _commandManager;
	private MultiplayerServer _networkHandler;

	public ServerGameState(MultiplayerServer server, boolean localhost)
	{
		_resourceManager = buildResourceManager();
		_pluginLoader = buildPluginLoader(_resourceManager);
		_gameLoop = new ServerGameLoop(this, localhost);
		_sceneManager = new SceneListManager();
		_playerList = buildPlayerList(server);
		_commandManager = buildCommandManager(this);
		_networkHandler = server;
	}

	@Override
	public ServerGameLoop getGameLoop()
	{
		return _gameLoop;
	}

	@Override
	public SceneListManager getSceneManager()
	{
		return _sceneManager;
	}

	@Override
	public ServerPlayerList getPlayerList()
	{
		return _playerList;
	}

	@Override
	public ResourceManager getResourceManager()
	{
		return _resourceManager;
	}

	public PluginLoader getPluginLoader()
	{
		return _pluginLoader;
	}

	public CommandManager getCommandManager()
	{
		return _commandManager;
	}

	@Override
	public MultiplayerServer getNetworkHandler()
	{
		return _networkHandler;
	}
}
