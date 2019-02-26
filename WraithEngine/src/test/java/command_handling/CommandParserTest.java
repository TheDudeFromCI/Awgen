package command_handling;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.we.command.CommandExecution;
import net.whg.we.command.CommandParser;
import net.whg.we.command.CommandSender;
import net.whg.we.command.CommandSet;
import net.whg.we.command.CommandVariable;
import net.whg.we.command.VariableKeyring;
import net.whg.we.ui.terminal.TerminalKeyring;

public class CommandParserTest
{
	private void validateCommand(CommandExecution exe, String name, String[] args,
			CommandVariable out)
	{
		Assert.assertEquals(name, exe.getCommand().getName());
		Assert.assertEquals(args.length, exe.getCommand().getArgs().length);
		Assert.assertEquals(out.getName(), exe.getOutput().getName());

		for (int i = 0; i < args.length; i++)
			Assert.assertEquals(args[i], exe.getCommand().getArg(i).toString());
	}

	private String[] args(String... args)
	{
		return args;
	}

	@Test
	public void parse1()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "clear");

		Assert.assertEquals(1, set.getVariableCount());
		Assert.assertEquals(1, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "clear", args(), vars.get(0));
	}

	@Test
	public void parse2()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "time -f ss");

		Assert.assertEquals(1, set.getVariableCount());
		Assert.assertEquals(1, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "time", args("-f", "ss"), vars.get(0));
	}

	@Test
	public void parse3()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "");

		Assert.assertEquals(0, set.getVariableCount());
		Assert.assertEquals(0, set.getCommandCount());
	}

	@Test
	public void parse4()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, ";;;");

		Assert.assertEquals(0, set.getVariableCount());
		Assert.assertEquals(0, set.getCommandCount());
	}

	@Test
	public void parse5()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "clear; time -f ss");

		Assert.assertEquals(2, set.getVariableCount());
		Assert.assertEquals(2, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "clear", args(), vars.get(0));
		validateCommand(cmds.get(1), "time", args("-f", "ss"), vars.get(1));
	}

	@Test
	public void parse6()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "$out = clear; time -f ss");

		Assert.assertEquals(2, set.getVariableCount());
		Assert.assertEquals(2, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "clear", args(), vars.get(0));
		validateCommand(cmds.get(1), "time", args("-f", "ss"), vars.get(1));
	}

	@Test
	public void parse7()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "$out = get1; $out = get2");

		Assert.assertEquals(1, set.getVariableCount());
		Assert.assertEquals(2, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "get1", args(), vars.get(0));
		validateCommand(cmds.get(1), "get2", args(), vars.get(0));
	}

	@Test
	public void parse8()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "a123 (abc -1; def -2)");

		Assert.assertEquals(3, set.getVariableCount());
		Assert.assertEquals(3, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "abc", args("-1"), vars.get(0));
		validateCommand(cmds.get(1), "def", args("-2"), vars.get(1));
		validateCommand(cmds.get(2), "a123", args("$1"), vars.get(2));
	}

	@Test
	public void parse9()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "a123 ($out1 = abc -1; $out2 = def -2)");

		Assert.assertEquals(3, set.getVariableCount());
		Assert.assertEquals(3, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "abc", args("-1"), vars.get(0));
		validateCommand(cmds.get(1), "def", args("-2"), vars.get(1));
		validateCommand(cmds.get(2), "a123", args("$out2"), vars.get(2));
	}

	@Test
	public void parse10()
	{
		CommandSender sender = Mockito.mock(CommandSender.class);
		VariableKeyring key = new TerminalKeyring();
		Mockito.when(sender.getVariableKeyring()).thenReturn(key);

		CommandSet set = CommandParser.parse(sender, "list $[hello world]");

		Assert.assertEquals(1, set.getVariableCount());
		Assert.assertEquals(1, set.getCommandCount());

		List<CommandExecution> cmds = set.getCommandExecutions();
		List<CommandVariable> vars = set.getVariables();

		validateCommand(cmds.get(0), "list", args("$[hello world]"), vars.get(0));
	}
}
