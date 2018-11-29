package net.whg.we.main;

public interface Plugin
{
	public String getPluginName();
	
	public void initPlugin();
	
	public void enablePlugin();
	
	public void disablePlugin();
	
	public int getInitalizePriority();
	
	public int getEnablePriority();
	
	public int getDisablePriority();
}
