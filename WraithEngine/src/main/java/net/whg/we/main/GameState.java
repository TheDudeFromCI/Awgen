package net.whg.we.main;

public class GameState
{
	private FileDatabase _fileDatabase;
	private ResourceLoader _resourceLoader;
	private PluginLoader _pluginLoader;
	private EventManager _eventManager;
	private GraphicsPipeline _graphics;

	public GameState(FileDatabase fileDatabase)
	{
		_fileDatabase = fileDatabase;
		_resourceLoader = new ResourceLoader(_fileDatabase);
		_pluginLoader = new PluginLoader();
		_eventManager = new EventManager();
		_graphics = new GraphicsPipeline(resourceLoader);
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
		_graphics.run();
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
