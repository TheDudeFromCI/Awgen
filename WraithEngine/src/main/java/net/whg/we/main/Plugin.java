package net.whg.we.main;

/**
 * Represents a plugin main class. There should be exactly one instance for each
 * plugin loaded. Each jar file in the plugin folder is representative of a
 * single plugin. Attempting to use multiple plugins per jar file may cause
 * unknown issues.
 * 
 * @author TheDudeFromCI
 */
public interface Plugin
{
	/**
	 * Gets the name of the plugin. This is static, and case sentitive.
	 * 
	 * @return The static name of the plugin.
	 */
	public String getPluginName();

	/**
	 * Initializes the plugin. This is preformed once, when a plugin is first
	 * added to the class path. This process can be used for linking to other
	 * plugins and building this plugin. No actions should be called on other
	 * plugins during this stage, as they may not be fully loaded at during this
	 * stage. This is used for functions that must be preformed only once while
	 * the program is running, and is not dependant on other plugins.
	 */
	public void initPlugin();

	/**
	 * This is called when the plugin is enabled. This can be called multiple
	 * times if plugins are unloaded or reloaded while the program is still
	 * running. This is called after initialize, and should be used for loading
	 * resources and calling methods in other plugins as needed. Event listeners
	 * should be attached during this stage.
	 * 
	 * @see initPlugin
	 * @see disablePlugin
	 */
	public void enablePlugin();

	/**
	 * This is called when the plugin is disabled. This can be called multiple
	 * times if plugins are unloaded or reloaded while the program is still
	 * running. This will always be called after enablePlugin(). This will be
	 * called during the closing operation of the program, and should be used to
	 * dispose and clean up resources.
	 * 
	 * @see initPlugin
	 * @see enablePlugin
	 */
	public void disablePlugin();

	/**
	 * Gets the priority of the plugin. This value represents the order in which
	 * plugins are enabled and disabled. Plugins with a lower value will be
	 * called before plugins with a higher value. The default value for a plugin
	 * is 0. Values can be negative. <br>
	 * <br>
	 * This value should not be changed during runtime.
	 * 
	 * @return The priority value for this plugin.
	 */
	public int getPriority();

	/**
	 * Checks if the plugin is currently enabled or not.
	 * 
	 * @return True if the plugin has been enabled. False otherwise.
	 */
	public boolean isEnabled();

	/**
	 * Checks if the plugin is currently initialized or not.
	 * 
	 * @return True if the plugin has been initialized. False otherwise.
	 */
	public boolean isInitialized();
}
