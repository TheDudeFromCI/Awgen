package command_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.command.Command;
import net.whg.frameworks.command.CommandConsole;
import net.whg.frameworks.command.CommandHandler;
import net.whg.frameworks.command.CommandList;
import net.whg.frameworks.command.CommandParser;
import net.whg.frameworks.command.CommandSender;
import net.whg.frameworks.command.CommandSet;
import net.whg.frameworks.command.DefaultKeyring;
import net.whg.frameworks.command.VariableKeyring;
import net.whg.frameworks.logging.Log;

public class CommandListTest
{
    @Test
    public void addCommandHandlers()
    {
        CommandHandler handler1 = Mockito.mock(CommandHandler.class);
        Mockito.when(handler1.getCommandName()).thenReturn("command");
        CommandHandler handler2 = Mockito.mock(CommandHandler.class);
        Mockito.when(handler2.getCommandName()).thenReturn("command2");

        CommandList list = new CommandList();
        Assert.assertEquals(0, list.getCommandCount());

        list.addCommand(handler1);
        Assert.assertEquals(1, list.getCommandCount());
        Assert.assertEquals(handler1, list.getCommand(0));
        Assert.assertEquals(handler1, list.getCommand("command"));

        list.addCommand(handler2);
        Assert.assertEquals(2, list.getCommandCount());
        Assert.assertEquals(handler2, list.getCommand(1));
        Assert.assertEquals(handler2, list.getCommand("command2"));

        list.removeCommand(handler1);
        list.removeCommand(handler2);
        Assert.assertEquals(0, list.getCommandCount());
    }

    @Test
    public void executeCommands()
    {
        CommandList list = new CommandList();

        CommandHandler clear = Mockito.mock(CommandHandler.class);
        Mockito.when(clear.getCommandName()).thenReturn("clear");
        Mockito.when(clear.getCommandAliases()).thenReturn(new String[0]);
        list.addCommand(clear);

        CommandHandler time = Mockito.mock(CommandHandler.class);
        Mockito.when(time.getCommandName()).thenReturn("time");
        Mockito.when(time.getCommandAliases()).thenReturn(new String[0]);
        list.addCommand(time);

        CommandHandler sleep = Mockito.mock(CommandHandler.class);
        Mockito.when(sleep.getCommandName()).thenReturn("sleep");
        Mockito.when(sleep.getCommandAliases()).thenReturn(new String[0]);
        list.addCommand(sleep);

        CommandSender sender = Mockito.mock(CommandSender.class);
        CommandConsole console = Mockito.mock(CommandConsole.class);
        VariableKeyring variables = new DefaultKeyring();
        Mockito.when(sender.getConsole()).thenReturn(console);
        Mockito.when(sender.getVariableKeyring()).thenReturn(variables);
        CommandSet set = CommandParser.parse(list, sender, "clear; time");

        list.executeCommandSet(set);

        Mockito.verify(clear).executeCommand(Mockito.any());
        Mockito.verify(time).executeCommand(Mockito.any());
        Mockito.verify(sleep, Mockito.never()).executeCommand(Mockito.any());
    }

    @Test
    public void getCommandByAlias()
    {
        CommandList list = new CommandList();

        CommandHandler clear = Mockito.mock(CommandHandler.class);
        Mockito.when(clear.getCommandName()).thenReturn("clear");
        Mockito.when(clear.getCommandAliases()).thenReturn(new String[]
        {
                "cl", "cr"
        });
        list.addCommand(clear);

        Assert.assertEquals(clear, list.getCommand("cr"));
    }

    @Test
    public void addNullCommand()
    {
        CommandList list = new CommandList();
        list.addCommand(null);

        Assert.assertEquals(0, list.getCommandCount());
    }

    @Test
    public void removeNullCommand()
    {
        CommandList list = new CommandList();
        list.addCommand(Mockito.mock(CommandHandler.class));

        list.removeCommand(null);

        Assert.assertEquals(1, list.getCommandCount());
    }

    @Test
    public void addCommandHandlerTwice()
    {
        CommandList list = new CommandList();

        CommandHandler clear = Mockito.mock(CommandHandler.class);

        list.addCommand(clear);
        list.addCommand(clear);

        Assert.assertEquals(1, list.getCommandCount());
    }

    @Test
    public void getUnknownCommand()
    {
        Assert.assertEquals(null, new CommandList().getCommand("clear"));
    }

    @Test
    public void addCommandsWhileRunning()
    {
        CommandList list = new CommandList();
        CommandHandler handler = new CommandHandler()
        {
            @Override
            public String getHelpText()
            {
                return null;
            }

            @Override
            public String getDescription()
            {
                return null;
            }

            @Override
            public String getCommandName()
            {
                return "clear";
            }

            @Override
            public String[] getCommandAliases()
            {
                return new String[0];
            }

            @Override
            public String executeCommand(Command command)
            {
                list.addCommand(Mockito.mock(CommandHandler.class));
                list.removeCommand(this);
                return null;
            }
        };
        list.addCommand(handler);

        CommandSender sender = Mockito.mock(CommandSender.class);
        CommandConsole console = Mockito.mock(CommandConsole.class);
        VariableKeyring variables = new DefaultKeyring();
        Mockito.when(sender.getConsole()).thenReturn(console);
        Mockito.when(sender.getVariableKeyring()).thenReturn(variables);
        CommandSet set = CommandParser.parse(list, sender, "clear; time");
        list.executeCommandSet(set);

        Assert.assertEquals(1, list.getCommandCount());
        Assert.assertNotEquals(handler, list.getCommand(0));
    }

    @Test
    public void errorWhileRunningCommand()
    {
        Log.setLogLevel(Log.FATAL);

        CommandList list = new CommandList();
        CommandHandler clear = new CommandHandler()
        {
            @Override
            public String getHelpText()
            {
                return null;
            }

            @Override
            public String getDescription()
            {
                return null;
            }

            @Override
            public String getCommandName()
            {
                return "clear";
            }

            @Override
            public String[] getCommandAliases()
            {
                return new String[0];
            }

            @Override
            public String executeCommand(Command command)
            {
                throw new RuntimeException();
            }
        };
        list.addCommand(clear);

        CommandHandler time = Mockito.mock(CommandHandler.class);
        Mockito.when(time.getCommandName()).thenReturn("time");
        list.addCommand(time);

        CommandSender sender = Mockito.mock(CommandSender.class);
        CommandConsole console = Mockito.mock(CommandConsole.class);
        VariableKeyring variables = new DefaultKeyring();
        Mockito.when(sender.getConsole()).thenReturn(console);
        Mockito.when(sender.getVariableKeyring()).thenReturn(variables);
        CommandSet set = CommandParser.parse(list, sender, "clear; time");
        list.executeCommandSet(set);

        Mockito.verify(time).executeCommand(Mockito.any());
    }
}