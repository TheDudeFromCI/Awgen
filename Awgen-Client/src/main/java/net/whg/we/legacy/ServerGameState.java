package net.whg.we.legacy;

import java.io.File;
import net.whg.frameworks.command.CommandManager;
import net.whg.frameworks.network.multiplayer.MultiplayerServer;
import net.whg.frameworks.network.server.ServerPlayerList;
import net.whg.frameworks.resource.FileDatabase;
import net.whg.frameworks.resource.ResourceDatabase;
import net.whg.frameworks.resource.ResourceLoader;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.commands.CommandUtils;
import net.whg.we.main.GameState;
import net.whg.we.main.PluginLoader;
import net.whg.we.render.HeadlessGraphics;
import net.whg.we.resource.MeshConverterLoader;
import net.whg.we.resource.MeshLoader;
import net.whg.we.resource.ServerFileDatabase;
import net.whg.we.resource.ShaderConverterLoader;
import net.whg.we.resource.ShaderLoader;
import net.whg.we.resource.SimpleFileDatabase;
import net.whg.we.resource.TextureConverterLoader;
import net.whg.we.resource.TextureLoader;
import net.whg.we.scene.SceneListManager;

public class ServerGameState implements GameState
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

	private static ServerPlayerList buildPlayerList(MultiplayerServer server)
	{
		return new ServerPlayerList(server.getPacketManager());
	}

	private static PluginLoader buildPluginLoader(ResourceManager resourceManager)
	{
		PluginLoader pluginLoader = new PluginLoader();

		pluginLoader.loadPluginsFromFile((ServerFileDatabase) resourceManager.getFileDatabase());
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
	private HeadlessGraphics _graphics;

	public ServerGameState(MultiplayerServer server, boolean localhost)
	{
		_graphics = new HeadlessGraphics();
		_resourceManager = buildResourceManager(_graphics);
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

	public Graphics getGraphics()
	{
		return _graphics;
	}
}
