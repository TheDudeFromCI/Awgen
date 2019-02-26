package net.whg.we.command.common;

import net.whg.we.command.Command;
import net.whg.we.command.CommandArgument;
import net.whg.we.command.CommandConsole;
import net.whg.we.command.CommandHandler;

public class SetCommand implements CommandHandler
{
	private final String[] ALIAS = {};

	@Override
	public String getCommandName()
	{
		return "set";
	}

	@Override
	public String[] getCommandAliases()
	{
		return ALIAS;
	}

	@Override
	public String executeCommand(Command command)
	{
		CommandArgument[] args = command.getArgs();
		CommandConsole console = command.getCommandSender().getConsole();

		if (args.length != 1)
		{
			console.println("Unknown number of parameters! Please use as:\n" + "set <arg>\n");
			return "";
		}

		return args[0].getValue();
	}

	@Override
	public String getDescription()
	{
		return "Returns the input argument. Used for variable assignment.";
	}

	@Override
	public String getHelpText()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("set <arg>\n");
		sb.append("  Returns the input argument.\n");

		return sb.toString();
	}
}
