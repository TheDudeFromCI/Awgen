package net.whg.we.command;

public class DynamicVariableArgument implements CommandArgument
{
	private String _line;

	public DynamicVariableArgument(String line)
	{
		_line = line;
	}

	@Override
	public String getValue()
	{
		CommandVariable var = CommandParser.parse(_line).getFinalOutput();

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
