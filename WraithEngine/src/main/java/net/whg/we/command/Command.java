package net.whg.we.command;

public class Command
{
	private String _name;
	private String[] _args;

	public Command(String name, String[] args)
	{
		_name = name;
		_args = args;
	}

	public String getName()
	{
		return _name;
	}

	public String[] getArgs()
	{
		return _args;
	}

	public void setArg(int index, String value)
	{
		_args[index] = value;
	}

	public String getArg(int index)
	{
		return _args[index];
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(_name);

		for (String s : _args)
			sb.append(" ").append(s);

		return sb.toString();
	}
}
