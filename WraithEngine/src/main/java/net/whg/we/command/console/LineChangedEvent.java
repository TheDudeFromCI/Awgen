package net.whg.we.command.console;

import net.whg.we.utils.Poolable;

public class LineChangedEvent implements Poolable
{
    private int _line;
    private String _text;
    private boolean _cancel;
    private String _oldText;

    @Override
    public void init()
    {
    }

    @Override
    public void dispose()
    {
    }

    public LineChangedEvent build(int line, String text, String oldText)
    {
        _line = line;
        _text = text;
        _oldText = oldText;
        _cancel = false;

        return this;
    }

    public int getLine()
    {
        return _line;
    }

    public String getText()
    {
        return _text;
    }

    public String getOldText()
    {
        return _oldText;
    }

    public void setCancelled(boolean cancel)
    {
        _cancel = cancel;
    }

    public boolean isCancelled()
    {
        return _cancel;
    }

    public void setText(String text)
    {
        _text = text;
    }
}
