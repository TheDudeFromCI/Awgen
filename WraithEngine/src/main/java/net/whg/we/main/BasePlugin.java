package net.whg.we.main;

/**
 * A base class for handling basic plugin functionalites. Override methods as
 * needed.
 * 
 * @author TheDudeFromCI
 */
public abstract class BasePlugin implements Plugin
{
	private boolean _enabled;
	private boolean _initialized;

	/**
	 * Base for initializing the plugin. If overriden, use "super.initPlugin()"
	 * for correct handling of other methods in this class.
	 */
	@Override
	public void initPlugin()
	{
		_initialized = true;
	}

	/**
	 * Base for enabling the plugin. If overriden, use "super.enablePlugin()"
	 * for correct handling of other methods in this class.
	 */
	@Override
	public void enablePlugin()
	{
		_enabled = true;
	}

	/**
	 * Base for disabling the plugin. If overriden, use "super.disablePlugin()"
	 * for correct handling of other methods in this class.
	 */
	@Override
	public void disablePlugin()
	{
		_enabled = false;
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public boolean isEnabled()
	{
		return _enabled;
	}

	@Override
	public boolean isInitialized()
	{
		return _initialized;
	}

}
