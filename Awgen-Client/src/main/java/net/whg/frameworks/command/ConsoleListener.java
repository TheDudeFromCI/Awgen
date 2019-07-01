package net.whg.frameworks.command;

import net.whg.frameworks.event.Listener;

public interface ConsoleListener extends Listener
{
    void onLineChanged(LineChangedEvent event);

    void onScrollPosChanged(ScrollPosChanged event);
}
