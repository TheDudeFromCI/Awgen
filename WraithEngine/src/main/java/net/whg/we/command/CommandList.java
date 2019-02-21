package net.whg.we.command;

import net.whg.we.utils.ComponentList;
import net.whg.we.utils.logging.Log;

public class CommandList
{
	private ComponentList<CommandHandler> _commands = new ComponentList<>();
	private boolean _running;

	public void addCommand(CommandHandler handler)
	{
		if (handler == null)
			return;

		if (_running)
			_commands.add(handler);
		else
			_commands.addInstant(handler);
	}

	public void removeCommand(CommandHandler handler)
	{
		if (handler == null)
			return;

		if (_running)
			_commands.remove(handler);
		else
			_commands.removeInstant(handler);
	}

	public boolean executeCommand(Command command)
	{
		try
		{
			_running = true;

			CommandHandler handler = getCommand(command.getName());

			if (handler != null)
			{
				handler.executeCommand(command);
				return true;
			}
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to execute command '%s'!", exception, command.toString());
		}
		finally
		{
			_running = false;
		}

		return false;
	}

	public CommandHandler getCommand(String name)
	{
		for (CommandHandler handler : _commands)
		{
			if (handler.getCommandName().equals(name))
				return handler;

			for (String s : handler.getCommandAliases())
				if (s.equals(name))
					return handler;
		}

		return null;
	}

	public int getCommandCount()
	{
		return _commands.size();
	}

	public CommandHandler getCommand(int index)
	{
		return _commands.get(index);
	}
}
