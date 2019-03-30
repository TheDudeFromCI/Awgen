package net.whg.we.scene;

import java.io.File;
import net.whg.we.client_logic.resources.FileDatabase;
import net.whg.we.client_logic.resources.ResourceDatabase;
import net.whg.we.client_logic.resources.ResourceLoader;
import net.whg.we.client_logic.resources.ResourceManager;
import net.whg.we.client_logic.resources.SimpleFileDatabase;
import net.whg.we.client_logic.resources.graphics.GLSLShaderLoader;
import net.whg.we.client_logic.resources.graphics.MeshLoader;
import net.whg.we.client_logic.resources.graphics.TextureLoader;
import net.whg.we.client_logic.resources.scene.FontLoader;
import net.whg.we.client_logic.resources.scene.MaterialLoader;
import net.whg.we.client_logic.resources.scene.ModelLoader;
import net.whg.we.command.CommandManager;
import net.whg.we.commands.CommandUtils;
import net.whg.we.main.PluginLoader;
import net.whg.we.network.multiplayer.MultiplayerServer;
import net.whg.we.network.server.ServerPlayerList;

public class ServerGameState implements GameState
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
	private ServerSceneManager _sceneManager;
	private ServerPlayerList _playerList;
	private CommandManager _commandManager;
	private MultiplayerServer _networkHandler;

	public ServerGameState(MultiplayerServer server, boolean localhost)
	{
		_resourceManager = buildResourceManager();
		_pluginLoader = buildPluginLoader(_resourceManager);
		_gameLoop = new ServerGameLoop(this, localhost);
		_sceneManager = new ServerSceneManager();
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
	public ServerSceneManager getSceneManager()
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
