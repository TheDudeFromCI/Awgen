package net.whg.we.server_logic.command.common;

import net.whg.we.server_logic.command.Command;
import net.whg.we.server_logic.command.CommandConsole;
import net.whg.we.server_logic.command.CommandHandler;

public class ClearCommand implements CommandHandler
{
    private final String[] ALIAS = {};

    @Override
    public String getCommandName()
    {
        return "clear";
    }

    @Override
    public String[] getCommandAliases()
    {
        return ALIAS;
    }

    @Override
    public String executeCommand(Command command)
    {
        CommandConsole console = command.getCommandSender().getConsole();

        if (command.getArgs().length > 0)
        {
            console.println("Unknown number of parameters!");
            return "";
        }

        console.clear();
        return "";
    }

    @Override
    public String getDescription()
    {
        return "Prints the input arguments, seperated by spaces.";
    }

    @Override
    public String getHelpText()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("clear\n");
        sb.append("  Clears the console.\n");

        return sb.toString();
    }
}
