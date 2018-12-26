package net.whg.we.main;

/**
 * Represents a plugin main class.
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
	 * Initializes the plugin. This is preformed once, when a plugin is first added
	 * to the class path. This process should be used for registering event callers
	 * and file loaders. This method may be called before other plugins or libraries
	 * are loaded.
	 */
	public void initPlugin();

	/**
	 * This method is called after all plugins are loaded and can be used for
	 * linking to other plugins or libraries.
	 */
	public void enablePlugin();

	/**
	 * Gets the priority of the plugin. This value represents the order in which
	 * plugins are enabled. Plugins with a lower value will be called before plugins
	 * with a higher value. The default value for a plugin is 0. Values can be
	 * negative.
	 *
	 * @return The priority value for this plugin.
	 */
	public int getPriority();
}
