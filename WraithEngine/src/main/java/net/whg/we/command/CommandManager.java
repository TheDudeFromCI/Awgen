package net.whg.we.command;

import net.whg.we.command.common.*;
import net.whg.we.ui.terminal.TerminalKeyring;

public class CommandManager
{
    private CommandList _commandList;
    private VariableKeyring _variableKeyRing;

    public CommandManager()
    {
        _commandList = new CommandList();
        _commandList.addCommand(new HelpCommand(_commandList));
        _commandList.addCommand(new PrintCommand());
        _commandList.addCommand(new ClearCommand());
        _commandList.addCommand(new SetCommand());
        _commandList.addCommand(new RandomCommand());
        _commandList.addCommand(new ForCommand());
        _commandList.addCommand(new ListCommand());

        _variableKeyRing = new TerminalKeyring();
    }

    public CommandList getCommandList()
    {
        return _commandList;
    }

    public VariableKeyring getVariableKeyring()
    {
        return _variableKeyRing;
    }

    public void execute(String command, CommandSender sender)
    {
        CommandSet commandSet =
                CommandParser.parse(_commandList, sender, command);
        _commandList.executeCommandSet(commandSet);
    }
}
