package net.whg.we.main;

import net.whg.we.client_logic.resources.ResourceManager;
import net.whg.we.client_logic.resources.graphics.GLSLShaderLoader;
import net.whg.we.client_logic.resources.graphics.MeshLoader;
import net.whg.we.client_logic.resources.graphics.TextureLoader;
import net.whg.we.client_logic.resources.scene.FontLoader;
import net.whg.we.client_logic.resources.scene.MaterialLoader;
import net.whg.we.client_logic.resources.scene.ModelLoader;
import net.whg.we.command.CommandManager;
import net.whg.we.commands.CommandUtils;
import net.whg.we.event.EventManager;
import net.whg.we.scene.GameLoop;

public class GameState
{
	private ResourceManager _resourceManager;
	private PluginLoader _pluginLoader;
	private EventManager _eventManager;
	private GameLoop _gameLoop;
	private CommandManager _commandManager;
	private boolean _isClient;

	public GameState(ResourceManager resourceManager, GameLoop gameLoop, boolean isClient)
	{
		_resourceManager = resourceManager;
		_pluginLoader = new PluginLoader();
		_eventManager = new EventManager();
		_commandManager = new CommandManager();// TODO Command manager should
												// only exist on the server game
												// state.
		_gameLoop = gameLoop;
		_isClient = isClient;

		if (!isClient)
			CommandUtils.addAdvancedCommands(_commandManager.getCommandList(), this);
	}

	public void run()
	{
		// Load file loaders
		_resourceManager.getResourceLoader().addFileLoader(new GLSLShaderLoader());
		_resourceManager.getResourceLoader().addFileLoader(new MeshLoader());
		_resourceManager.getResourceLoader().addFileLoader(new TextureLoader());
		_resourceManager.getResourceLoader()
				.addFileLoader(new MaterialLoader(_resourceManager.getFileDatabase()));
		_resourceManager.getResourceLoader()
				.addFileLoader(new ModelLoader(_resourceManager.getFileDatabase()));
		_resourceManager.getResourceLoader().addFileLoader(new FontLoader());

		// Load plugins
		_pluginLoader.loadPluginsFromFile(_resourceManager.getFileDatabase());
		_pluginLoader.enableAllPlugins();

		// Start game loop
		_gameLoop.run();
	}

	public ResourceManager getResourceManager()
	{
		return _resourceManager;
	}

	public PluginLoader getPluginLoader()
	{
		return _pluginLoader;
	}

	public EventManager getEventManager()
	{
		return _eventManager;
	}

	public GameLoop getGameLoop()
	{
		return _gameLoop;
	}

	public CommandManager getCommandManager()
	{
		return _commandManager;
	}

	public boolean isClient()
	{
		return _isClient;
	}
}
