package net.whg.we.main;

/**
 * This is the core plugin loader for WraithEngine. It is in charge of loading and sending events
 * between plugins.
 * 
 * @author TheDudeFromCI
 */
public class CorePlugin extends BasePlugin
{
	@Override
	public String getPluginName()
	{
		return "Core";
	}

	@Override
	public int getPriority()
	{
		return 1000;
	}
}
