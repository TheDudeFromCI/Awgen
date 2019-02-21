package net.whg.we.command;

import java.util.LinkedList;
import java.util.List;

public class CommandSet
{
	private List<CommandVariable> _variables = new LinkedList<>();
	private List<CommandExecution> _commands = new LinkedList<>();

	public void insertCommandExecution(CommandExecution execution)
	{
		if (execution == null)
			return;

		if (_commands.contains(execution))
			return;

		_commands.add(execution);
	}

	public CommandVariable getVariable(String name)
	{
		if (name == null)
			return null;

		for (CommandVariable variable : _variables)
			if (variable.getName().equals(name))
				return variable;
		return null;
	}

	public CommandVariable getOrCreateVariable(String name)
	{
		CommandVariable var = getVariable(name);
		if (var != null)
			return var;

		var = new CommandVariable(name);
		addVariable(var);
		return var;
	}

	public void addVariable(CommandVariable variable)
	{
		if (variable == null)
			return;

		if (_variables.contains(variable))
			return;

		_variables.add(variable);
	}

	public List<CommandExecution> getCommandExecutions()
	{
		return _commands;
	}

	public List<CommandVariable> getVariables()
	{
		return _variables;
	}

	public void clear()
	{
		_variables.clear();
		_commands.clear();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (CommandExecution exe : _commands)
		{
			sb.append("$").append(exe.getOutput().getName()).append(" = ")
					.append(exe.getCommand().getName());
			for (CommandArgument arg : exe.getCommand().getArgs())
				sb.append(" ").append(arg);
			sb.append('\n');
		}

		return sb.toString();
	}

	public int getVariableCount()
	{
		return _variables.size();
	}

	public int getCommandCount()
	{
		return _commands.size();
	}

	public CommandVariable getFinalOutput()
	{
		if (_commands.size() == 0)
			return null;
		return _commands.get(getCommandCount() - 1).getOutput();
	}
}
