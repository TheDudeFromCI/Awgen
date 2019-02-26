package net.whg.we.command;

public class CommandParser
{
	public static CommandSet parse(CommandSender sender, String line)
	{
		CommandSet set = new CommandSet();
		parse(set, line, sender);
		return set;
	}

	private static CommandVariable parse(CommandSet set, String line, CommandSender sender)
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

			if (tokens[commandTokenIndex].getType() != TokenTemplate.STANDARD)
			{
				if (tokens[0].getType() == TokenTemplate.VARIABLE)
					lastVar = set.getOrCreateVariable(tokens[0].getValue().substring(1));
				else
					lastVar = set.getOrCreateVariable(String.valueOf(set.getVariableCount()));

				Command command = new Command("set", new CommandArgument[]
				{
						asCommandArgument(set, tokens[commandTokenIndex], sender)
				}, sender);
				CommandExecution exe = new CommandExecution(command, lastVar);
				set.insertCommandExecution(exe);

				continue;
			}

			String commandName = tokens[commandTokenIndex].getValue();

			CommandArgument[] args;
			if (tokens[tokens.length - 1].getType() == TokenTemplate.SYMBOL)
				args = new CommandArgument[tokens.length - commandTokenIndex - 2];
			else
				args = new CommandArgument[tokens.length - commandTokenIndex - 1];

			for (int i = 0; i < args.length; i++)
			{
				int a = commandTokenIndex + i + 1;
				String val = tokens[a].getValue();

				if (tokens[a].getType() == TokenTemplate.NESTED_COMMAND)
				{
					CommandVariable v2 = parse(set, val, sender);
					if (v2 == null)
						args[i] = new StringArgument("");
					else
						args[i] = new VariableArgument(v2);
				}
				else
					args[i] = asCommandArgument(set, tokens[a], sender);
			}

			if (tokens[0].getType() == TokenTemplate.VARIABLE)
				lastVar = set.getOrCreateVariable(tokens[0].getValue().substring(1));
			else
				lastVar = set.getOrCreateVariable(String.valueOf(set.getVariableCount()));

			Command command = new Command(commandName, args, sender);
			CommandExecution exe = new CommandExecution(command, lastVar);
			set.insertCommandExecution(exe);
		}

		return lastVar;
	}

	private static CommandArgument asCommandArgument(CommandSet set, Token token,
			CommandSender sender)
	{
		switch (token.getType())
		{
			case TokenTemplate.STANDARD:
			case TokenTemplate.STRING:
				return new StringArgument(token.getValue());

			case TokenTemplate.VARIABLE:
				return new VariableArgument(set.getOrCreateVariable(token.getValue().substring(1)));

			case TokenTemplate.DYNAMIC_VARIABLE:
				return new DynamicVariableArgument(sender,
						token.getValue().substring(2, token.getValue().length() - 1));

			default:
				throw new CommandParseException("Unknown token type! " + token.getType());
		}
	}

}
