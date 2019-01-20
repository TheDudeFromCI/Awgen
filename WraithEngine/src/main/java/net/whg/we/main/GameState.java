package net.whg.we.main;

import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.event.EventManager;
import net.whg.we.rendering.GraphicsPipeline;
import net.whg.we.resources.GLSLShaderLoader;
import net.whg.we.resources.MeshSceneLoader;
import net.whg.we.resources.TextureLoader;

public class GameState
{
	private FileDatabase _fileDatabase;
	private ResourceLoader _resourceLoader;
	private PluginLoader _pluginLoader;
	private EventManager _eventManager;
	private GraphicsPipeline _graphicsPipeline;

	public GameState(FileDatabase fileDatabase)
	{
		_fileDatabase = fileDatabase;
		_resourceLoader = new ResourceLoader(_fileDatabase);
		_pluginLoader = new PluginLoader();
		_eventManager = new EventManager();
		_graphicsPipeline = new GraphicsPipeline(_resourceLoader);
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
		_graphicsPipeline.run();
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

	public GraphicsPipeline getGraphicsPipeline()
	{
		return _graphicsPipeline;
	}
}
