package net.whg.awgen.server.commands;

import net.whg.awgen.server.core.GameState;
import net.whg.awgenshell.ArgumentValue;
import net.whg.awgenshell.CommandHandler;
import net.whg.awgenshell.CommandResult;
import net.whg.awgenshell.ShellEnvironment;
import net.whg.stlib.scene.SceneNode;

public class SceneCommand implements CommandHandler
{
	private static final String[] ALIASES = {};

	private GameState gameState;

	public SceneCommand(GameState gameState)
	{
		this.gameState = gameState;
	}

	@Override
	public CommandResult execute(ShellEnvironment shell, ArgumentValue[] args)
	{
		if (args.length == 0)
		{
			StringBuilder sb = new StringBuilder();
			walkSceneTree(sb, gameState.getScene().getRoot(), 0);
			shell.getCommandSender().println(sb.toString());

			return new CommandResult("", true, true);
		}

		if (args.length == 2)
		{
			String a0 = args[0].getValue();

			if (a0.equalsIgnoreCase("remove"))
			{
				String a1 = args[1].getValue();

				SceneNode node = gameState.getScene().findNode(a1);
				if (node == null)
				{
					shell.getCommandSender().println("Node not found: '" + a1 + "'!");
					return CommandResult.ERROR;
				}

				if (node.getParent() == null)
					gameState.getScene().setRoot(new SceneNode());
				else
					node.setParent(null);

				return new CommandResult("", true, true);
			}

			shell.getCommandSender().println("Unknown subcommmand: '" + a0 + "'!");
			return CommandResult.ERROR;
		}

		if (args.length == 3)
		{
			String a0 = args[0].getValue();

			if (a0.equalsIgnoreCase("add"))
			{
				String a1 = args[1].getValue();
				String a2 = args[2].getValue();

				SceneNode parent = gameState.getScene().findNode(a1);
				if (parent == null)
				{
					shell.getCommandSender().println("Node not found: '" + a1 + "'!");
					return CommandResult.ERROR;
				}

				SceneNode child = newNode(a2);
				if (child == null)
				{
					shell.getCommandSender().println("Node type unrecognized: '" + a2 + "'!");
					return CommandResult.ERROR;
				}

				parent.addChild(child);

				return new CommandResult("", true, true);
			}

			if (a0.equalsIgnoreCase("rename"))
			{
				String a1 = args[1].getValue();
				String a2 = args[2].getValue();

				SceneNode node = gameState.getScene().findNode(a1);
				if (node == null)
				{
					shell.getCommandSender().println("Node not found: '" + a1 + "'!");
					return CommandResult.ERROR;
				}

				node.setName(a2);

				return new CommandResult("", true, true);
			}

			shell.getCommandSender().println("Unknown subcommmand: '" + a0 + "'!");
			return CommandResult.ERROR;
		}

		shell.getCommandSender().println("Unknown number of arguments!");
		return CommandResult.ERROR;
	}

	private SceneNode newNode(String type)
	{
		switch (type.toLowerCase())
		{
			case "empty":
				return new SceneNode();

			default:
				return null;
		}
	}

	private void walkSceneTree(StringBuilder sb, SceneNode node, int depth)
	{
		for (int i = 0; i < depth; i++)
			sb.append("  ");

		sb.append(node.getNodeType()).append(": ").append(node.getName());
		sb.append(" (").append(node.getUUID()).append(")\n");

		for (int i = 0; i < node.getChildCount(); i++)
			walkSceneTree(sb, node.getChild(i), depth + 1);
	}

	@Override
	public String[] getAliases()
	{
		return ALIASES;
	}

	@Override
	public String getName()
	{
		return "scene";
	}
}
