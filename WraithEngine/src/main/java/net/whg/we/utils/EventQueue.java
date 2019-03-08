package net.whg.we.utils;

import java.util.LinkedList;

public class EventQueue
{
    private LinkedList<Runnable> _events = new LinkedList<>();

    public void addEvent(Runnable e)
    {
        if (e == null)
            return;

        synchronized (_events)
        {
            _events.add(e);
        }
    }

    public void runEvents()
    {
        Runnable r;

        while (true)
        {
            synchronized (_events)
            {
                if (_events.isEmpty())
                    return;

                r = _events.removeFirst();
            }

            r.run();
        }
    }
}
