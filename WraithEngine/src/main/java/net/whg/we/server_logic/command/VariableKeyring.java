package net.whg.we.server_logic.command;

import java.util.List;

public interface VariableKeyring
{
    CommandVariable getVariable(String name);

    boolean hasVariable(String name);

    void addVariable(CommandVariable var);

    List<CommandVariable> getVariables();

    void clearTemp();
}
