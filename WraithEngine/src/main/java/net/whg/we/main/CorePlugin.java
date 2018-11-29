package net.whg.we.main;

public class CorePlugin implements Plugin
{
	@Override
	public String getPluginName()
	{
		return "Core";
	}

	@Override
	public void initPlugin()
	{
	}

	@Override
	public void enablePlugin()
	{
	}

	@Override
	public void disablePlugin()
	{
	}

	@Override
	public int getInitalizePriority()
	{
		return 0;
	}

	@Override
	public int getEnablePriority()
	{
		return 1000;
	}

	@Override
	public int getDisablePriority()
	{
		return 0;
	}
}
