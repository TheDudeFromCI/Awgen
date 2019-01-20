package net.whg.we.main;

public class GameState
{
	private FileDatabase _fileDatabase;
	private ResourceLoader _resourceLoader;
	private PluginLoader _pluginLoader;
	private GraphicsPipeline _graphics;

	public GameState(FileDatabase fileDatabase)
	{
		_fileDatabase = fileDatabase;

		_resourceLoader = new ResourceLoader(_fileDatabase);
		_resourceLoader.addFileLoader(new GLSLShaderLoader());
		_resourceLoader.addFileLoader(new MeshSceneLoader());
		_resourceLoader.addFileLoader(new TextureLoader());

		_pluginLoader = new PluginLoader();
		_pluginLoader.loadPluginsFromFile(_fileDatabase);
		_pluginLoader.enableAllPlugins();

		_graphics = new GraphicsPipeline(resourceLoader);
	}

	public void run()
	{
		_graphics.run();
	}
}
