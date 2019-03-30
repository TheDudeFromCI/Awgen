package net.whg.we.commands;

import net.whg.we.command.Command;
import net.whg.we.command.CommandHandler;
import net.whg.we.command.CommandSender;
import net.whg.we.network.server.OnlinePlayer;
import net.whg.we.network.server.PlayerCommandSender;
import net.whg.we.scene.Scene;
import net.whg.we.scene.SceneNode;

public class SceneHierarchyCommand implements CommandHandler
{
	private final String[] ALIAS = {};

	@Override
	public String getCommandName()
	{
		return "scene_hierarchy";
	}

	@Override
	public String[] getCommandAliases()
	{
		return ALIAS;
	}

	@Override
	public String getDescription()
	{
		return "Builds a tree view of the current scene.";
	}

	@Override
	public String getHelpText()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("scene_hierarchy\n");
		sb.append("  Returns a tree view of the current scene the player is in.\n");

		return sb.toString();
	}

	@Override
	public String executeCommand(Command command)
	{
		CommandSender sender = command.getCommandSender();

		if (command.getArgs().length != 0)
		{
			sender.println("Unknown number of parameters!");
			return "";
		}

		if (!(sender instanceof PlayerCommandSender))
		{
			sender.println("This command can only be preformed as a player!");
			return "";
		}

		OnlinePlayer player = ((PlayerCommandSender) sender).getPlayer();
		Scene scene = player.getScene();

		StringBuilder sb = new StringBuilder();
		buildTree(sb, scene.getSceneNode(), 0);

		return sb.toString();
	}

	private void buildTree(StringBuilder sb, SceneNode node, int indent)
	{
		for (int i = 0; i < indent; i++)
			sb.append("  ");
		sb.append(node.getName()).append("\n");

		indent++;

		for (int i = 0; i < node.getChildCount(); i++)
			buildTree(sb, node.getChild(i), indent);
	}
}
