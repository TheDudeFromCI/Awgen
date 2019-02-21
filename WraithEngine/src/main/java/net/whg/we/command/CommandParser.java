package net.whg.we.command;

public class CommandParser
{
	public static CommandSet parse(String line)
	{
		CommandSet set = new CommandSet();
		parse(set, line);
		return set;
	}

	private static CommandVariable parse(CommandSet set, String line)
	{
		TokenTree tree = new TokenTree();
		tree.addTokens(new Tokenizer(line));

		CommandVariable lastVar = null;
		while (tree.hasNextPath())
		{
			TokenPath path = tree.nextPath();
			Token[] tokens = path.getTokens();

			if (tokens.length == 0)
				continue;
			if (tokens.length == 1 && tokens[0].getType() == TokenTemplate.SYMBOL
					&& tokens[0].getValue().equals(";"))
				continue;

			int commandTokenIndex = 0;

			if (tokens[0].getType() == TokenTemplate.VARIABLE)
				commandTokenIndex = 2;

			String commandName = tokens[commandTokenIndex].getValue();

			String[] args;
			if (tokens[tokens.length - 1].getType() == TokenTemplate.SYMBOL)
				args = new String[tokens.length - commandTokenIndex - 2];
			else
				args = new String[tokens.length - commandTokenIndex - 1];

			for (int i = 0; i < args.length; i++)
			{
				int a = commandTokenIndex + i + 1;
				String val = tokens[a].getValue();

				if (tokens[a].getType() == TokenTemplate.NESTED_COMMAND)
				{
					CommandVariable v2 = parse(set, val);
					if (v2 == null)
						val = "";
					else
						val = "$" + v2.getName();
				}

				args[i] = val;
			}

			if (tokens[0].getType() == TokenTemplate.VARIABLE)
				lastVar = set.getOrCreateVariable(tokens[0].getValue().substring(1));
			else
				lastVar = set.getOrCreateVariable(String.valueOf(set.getVariableCount()));

			Command command = new Command(commandName, args);
			CommandExecution exe = new CommandExecution(command, lastVar);
			set.insertCommandExecution(exe);
		}

		return lastVar;
	}
}
