package command_handling;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.command.Command;
import net.whg.frameworks.command.CommandArgument;
import net.whg.frameworks.command.CommandConsole;
import net.whg.frameworks.command.CommandExecution;
import net.whg.frameworks.command.CommandHandler;
import net.whg.frameworks.command.CommandList;
import net.whg.frameworks.command.CommandParser;
import net.whg.frameworks.command.CommandSender;
import net.whg.frameworks.command.CommandSet;
import net.whg.frameworks.command.DefaultKeyring;
import net.whg.frameworks.command.VariableKeyring;

public class CommandSetTest
{
    @Test
    public void setToString()
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

        CommandSender sender = Mockito.mock(CommandSender.class);
        CommandConsole console = Mockito.mock(CommandConsole.class);
        VariableKeyring variables = new DefaultKeyring();
        Mockito.when(sender.getConsole()).thenReturn(console);
        Mockito.when(sender.getVariableKeyring()).thenReturn(variables);

        CommandSet set = CommandParser.parse(list, sender, "clear; time -f ss");
        Assert.assertEquals("$0 = clear\n$1 = time -f ss", set.toString());

        variables.clearTemp();
        set = CommandParser.parse(list, sender, "$red=clear");
        Assert.assertEquals("$red = clear", set.toString());

        // Here we have to clear all variables, as '$red' is in the list, still
        variables.getVariables().clear();

        set = CommandParser.parse(list, sender, "time $[loop]");
        Assert.assertEquals("$0 = time $[loop]", set.toString());
    }

    @Test
    public void clear()
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

        CommandSender sender = Mockito.mock(CommandSender.class);
        CommandConsole console = Mockito.mock(CommandConsole.class);
        VariableKeyring variables = new DefaultKeyring();
        Mockito.when(sender.getConsole()).thenReturn(console);
        Mockito.when(sender.getVariableKeyring()).thenReturn(variables);

        CommandSet set = CommandParser.parse(list, sender, "clear; time -f ss");
        Assert.assertEquals(2, set.getCommandCount());

        set.clear();
        Assert.assertEquals(0, set.getCommandCount());
    }

    @Test
    public void variableOutputs()
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

        CommandSender sender = Mockito.mock(CommandSender.class);
        CommandConsole console = Mockito.mock(CommandConsole.class);
        VariableKeyring variables = new DefaultKeyring();
        Mockito.when(sender.getConsole()).thenReturn(console);
        Mockito.when(sender.getVariableKeyring()).thenReturn(variables);

        CommandSet set =
                CommandParser.parse(list, sender, "clear; $out = time -f ss");
        Assert.assertEquals(set.getVariable("out"), set.getFinalOutput());
    }

    @Test
    public void finalOutput_NoCommands()
    {
        Assert.assertNull(
                new CommandSet(new DefaultKeyring()).getFinalOutput());
    }

    @Test
    public void addNullVariable()
    {
        VariableKeyring variables = new DefaultKeyring();
        CommandSet set = new CommandSet(variables);

        set.addVariable(null);
        Assert.assertEquals(0, set.getVariableCount());
    }

    @Test
    public void getNullVariable()
    {
        VariableKeyring variables = new DefaultKeyring();
        CommandSet set = new CommandSet(variables);

        Assert.assertNull(set.getVariable(null));
    }

    @Test
    public void insertCommand_Null()
    {
        VariableKeyring variables = new DefaultKeyring();
        CommandSet set = new CommandSet(variables);

        set.insertCommandExecution(null);
        Assert.assertEquals(0, set.getCommandCount());
    }

    @Test
    public void insertCommand_Twice()
    {
        VariableKeyring variables = new DefaultKeyring();
        CommandSet set = new CommandSet(variables);

        Command com = new Command("clear", new CommandArgument[0], null);
        CommandExecution exe =
                new CommandExecution(com, set.getOrCreateVariable("var"));

        set.insertCommandExecution(exe);
        set.insertCommandExecution(exe);

        Assert.assertEquals(1, set.getCommandCount());
    }
}
