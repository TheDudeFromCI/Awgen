package net.whg.frameworks.command;

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

    public void executeCommandSet(CommandSet set)
    {
        try
        {
            _running = true;

            for (CommandExecution exe : set.getCommandExecutions())
            {
                Command cmd = exe.getCommand();
                CommandHandler handler = getCommand(cmd.getName());

                if (handler == null)
                {
                    cmd.getCommandSender().getConsole()
                            .println("Unknown command '" + cmd.getName() + "'");
                    continue;
                }

                try
                {
                    String out = handler.executeCommand(cmd);
                    exe.getOutput().setValue(out);
                }
                catch (Exception exception)
                {
                    Log.errorf("Failed to execute command '%s'!", exception,
                            cmd.toString());
                }
            }
        }
        finally
        {
            _running = false;
            _commands.endFrame();
        }
    }

    public CommandHandler getCommand(String name)
    {
        for (CommandHandler handler : _commands)
        {
            if (handler.getCommandName().equals(name))
                return handler;
        }

        for (CommandHandler handler : _commands)
        {
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
