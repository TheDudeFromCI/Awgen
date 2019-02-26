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
		return "Prints the input arguments, seperated by spaces.";
	}

	@Override
	public String getHelpText()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("print <args>\n");
		sb.append("  Prints the input arguments(s), seperated by spaces.\n");

		return sb.toString();
	}
}
