package net.whg.we.command;

public interface CommandConsole
{
	void println(String text);

	void clear();

	String getLine(int index);

	int getLineCount();

	void setLine(int line, String text);

	int getScrollPos();
}