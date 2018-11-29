package net.whg.we.main;

public class WraithEngine
{
	public static void main(String[] args)
	{
		// Create plugin loader
		PluginLoader pluginLoader = new PluginLoader();
		
		// Load core plugins
		pluginLoader.loadPlugin(new CorePlugin());
		
		// Initialize and enable plugins
		pluginLoader.initializeAllPlugins();
		pluginLoader.enableAllPlugins();
	}
}
