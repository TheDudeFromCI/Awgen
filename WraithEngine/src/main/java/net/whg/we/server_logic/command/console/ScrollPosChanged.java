package net.whg.we.server_logic.command.console;

import net.whg.we.utils.Poolable;

public class ScrollPosChanged implements Poolable
{
    private int _line;
    private int _oldLine;
    private boolean _cancel;

    @Override
    public void init()
    {
    }

    @Override
    public void dispose()
    {
    }

    public ScrollPosChanged build(int line, int oldLine)
    {
        _line = line;
        _oldLine = oldLine;
        _cancel = false;

        return this;
    }

    public int getLine()
    {
        return _line;
    }

    public int getOldLine()
    {
        return _oldLine;
    }

    public void setCancelled(boolean cancel)
    {
        _cancel = cancel;
    }

    public boolean isCancelled()
    {
        return _cancel;
    }
}
