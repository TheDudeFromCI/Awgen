package net.whg.we.server_logic.command;

public interface CommandSender
{
    CommandConsole getConsole();

    VariableKeyring getVariableKeyring();

    String getUsername();
}
