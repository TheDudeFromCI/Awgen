package net.whg.we.command;

public class DynamicVariableArgument implements CommandArgument
{
	private CommandSender _sender;
	private String _line;

	public DynamicVariableArgument(CommandSender sender, String line)
	{
		_sender = sender;
		_line = line;
	}

	@Override
	public String getValue()
	{
		CommandVariable var = CommandParser.parse(_sender, _line).getFinalOutput();

		if (var == null)
			return "";

		return var.getValue();
	}

	@Override
	public String toString()
	{
		return String.format("$[%s]", _line);
	}
}
