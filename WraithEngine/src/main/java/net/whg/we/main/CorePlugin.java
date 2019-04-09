package net.whg.we.main;

public class CorePlugin implements Plugin
{
    @Override
    public String getPluginName()
    {
        // TODO Change to Core
        return "TestPlugin";
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
    public int getPriority()
    {
        return 0;
    }
}
