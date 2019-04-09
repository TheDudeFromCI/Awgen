package net.whg.frameworks.event;

import net.whg.frameworks.util.Poolable;

public class PendingEvent implements Poolable
{
    private int _eventId;
    private Object _eventArg;

    @Override
    public void init()
    {

    }

    @Override
    public void dispose()
    {
        _eventId = -1;
        _eventArg = null;
    }

    public PendingEvent build(int eventId, Object eventArg)
    {
        _eventId = eventId;
        _eventArg = eventArg;

        return this;
    }

    public int getEventId()
    {
        return _eventId;
    }

    public Object getEventArg()
    {
        return _eventArg;
    }
}
