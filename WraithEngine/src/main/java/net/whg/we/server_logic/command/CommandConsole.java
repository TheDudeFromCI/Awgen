package net.whg.we.server_logic.command;

public interface CommandConsole
{
    void println(String text);

    void clear();

    String getLine(int index);

    int getLineCount();

    void setLine(int line, String text);

    int getScrollPos();
}
