package net.whg.we.main;

/**
 * The program entry class. This class is used for the purpose of initializing the plugin
 * loader, all core plugins, and hunting down and launching local plugins.
 * 
 * @author TheDudeFromCI
 */
public class WraithEngine
{
	/**
	 * The program entry method.
	 * 
	 * @param args - Arguments from command line. Used for determining program functions.
	 * Currently, there are no extra runtime flags that can be used at this
	 * time.
	 */
	public static void main(String[] args)
	{
		// Create plugin loader
		PluginLoader pluginLoader = new PluginLoader();
		
		// Load core plugins
		pluginLoader.loadPlugin(new CorePlugin());
		
		// Enable plugins
		pluginLoader.enableAllPlugins();
	}
}
