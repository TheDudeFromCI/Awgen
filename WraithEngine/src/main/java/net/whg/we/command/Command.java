package net.whg.we.command;

public class Command
{
	private String _name;
	private CommandArgument[] _args;

	public Command(String name, CommandArgument[] args)
	{
		_name = name;
		_args = args;
	}

	public String getName()
	{
		return _name;
	}

	public CommandArgument[] getArgs()
	{
		return _args;
	}

	public void setArg(int index, CommandArgument value)
	{
		_args[index] = value;
	}

	public CommandArgument getArg(int index)
	{
		return _args[index];
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(_name);

		for (CommandArgument s : _args)
			sb.append(" ").append(s.getValue());

		return sb.toString();
	}
}
