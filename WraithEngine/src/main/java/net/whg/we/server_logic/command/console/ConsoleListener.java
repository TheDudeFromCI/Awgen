package net.whg.we.server_logic.command.console;

import net.whg.we.event.Listener;

public interface ConsoleListener extends Listener
{
    void onLineChanged(LineChangedEvent event);

    void onScrollPosChanged(ScrollPosChanged event);
}
