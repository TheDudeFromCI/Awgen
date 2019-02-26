package net.whg.we.ui.terminal;

import net.whg.we.event.Listener;

public interface ConsoleListener extends Listener
{
	void onLineChanged(LineChangedEvent event);

	void onScrollPosChanged(ScrollPosChanged event);
}
