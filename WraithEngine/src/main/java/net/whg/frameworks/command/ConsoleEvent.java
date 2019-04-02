package net.whg.frameworks.command;

import net.whg.frameworks.util.ObjectPool;
import net.whg.frameworks.util.SimpleObjectPool;
import net.whg.we.event.EventCallerBase;

public class ConsoleEvent extends EventCallerBase<ConsoleListener>
{
    private static final int LINE_CHANGED_EVENT = 0;
    private static final int SCROLLPOS_CHANGED_EVENT = 1;

    private ObjectPool<LineChangedEvent> _pool1 =
            new SimpleObjectPool<>(LineChangedEvent.class);
    private ObjectPool<ScrollPosChanged> _pool2 =
            new SimpleObjectPool<>(ScrollPosChanged.class);

    void onLineChanged(int line, String text, String oldText)
    {
        callEvent(LINE_CHANGED_EVENT, _pool1.get().build(line, text, oldText));
    }

    void onScrollPosChanged(int line, int oldLine)
    {
        callEvent(SCROLLPOS_CHANGED_EVENT, _pool2.get().build(line, oldLine));
    }

    @Override
    protected void runEvent(ConsoleListener listener, int index, Object arg)
    {
        switch (index)
        {
            case LINE_CHANGED_EVENT:
                listener.onLineChanged((LineChangedEvent) arg);
                break;

            case SCROLLPOS_CHANGED_EVENT:
                listener.onScrollPosChanged((ScrollPosChanged) arg);
                break;

            default:
                throw new IllegalStateException("Unknown event!");
        }
    }
}
