package net.whg.we.main;

import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.event.EventManager;
import net.whg.we.resources.graphics.GLSLShaderLoader;
import net.whg.we.resources.graphics.MeshSceneLoader;
import net.whg.we.resources.graphics.TextureLoader;
import net.whg.we.scene.GameLoop;

public class GameState
{
	private FileDatabase _fileDatabase;
	private ResourceLoader _resourceLoader;
	private PluginLoader _pluginLoader;
	private EventManager _eventManager;
	private GameLoop _gameLoop;

	public GameState(FileDatabase fileDatabase, ResourceLoader resourceLoader, GameLoop gameLoop)
	{
		_fileDatabase = fileDatabase;
		_resourceLoader = resourceLoader;
		_pluginLoader = new PluginLoader();
		_eventManager = new EventManager();
		_gameLoop = gameLoop;
	}

	public void run()
	{
		// Load file loaders
		_resourceLoader.addFileLoader(new GLSLShaderLoader());
		_resourceLoader.addFileLoader(new MeshSceneLoader());
		_resourceLoader.addFileLoader(new TextureLoader());

		// Load plugins
		_pluginLoader.loadPluginsFromFile(_fileDatabase);
		_pluginLoader.enableAllPlugins();

		// Start game loop
		_gameLoop.run();
	}

	public FileDatabase getFileDatabase()
	{
		return _fileDatabase;
	}

	public ResourceLoader getResourceLoader()
	{
		return _resourceLoader;
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
}
