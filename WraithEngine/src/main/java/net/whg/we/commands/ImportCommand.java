package net.whg.we.commands;

import net.whg.frameworks.command.Command;
import net.whg.frameworks.command.CommandArgument;
import net.whg.frameworks.command.CommandHandler;
import net.whg.frameworks.command.CommandSender;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.server.OnlinePlayer;
import net.whg.frameworks.network.server.PlayerCommandSender;
import net.whg.we.packets.ImportRequestPacket;

public class ImportCommand implements CommandHandler
{
	private static final String[] ALIAS = {};

	@Override
	public String getCommandName()
	{
		return "import";
	}

	@Override
	public String[] getCommandAliases()
	{
		return ALIAS;
	}

	@Override
	public String getDescription()
	{
		return "Imports a file on the user's local computer to the server as an asset.";
	}

	@Override
	public String getHelpText()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("import <file path>\n");
		sb.append("  Imports a resource file from your hard drive to the server as a downloadable "
				+ "resource.\n");

		return sb.toString();
	}

	@Override
	public String executeCommand(Command command)
	{
		CommandSender sender = command.getCommandSender();
		CommandArgument[] args = command.getArgs();

		if (args.length != 1)
		{
			sender.println("Unknown number of parameters!");
			return "";
		}

		if (!(sender instanceof PlayerCommandSender))
		{
			sender.println("You must be a player to preform this command!");
			return "";
		}

		OnlinePlayer player = ((PlayerCommandSender) sender).getPlayer();

		Packet packet = player.newPacket("player.import_resource");
		ImportRequestPacket.build(packet, args[0].getValue());
		player.sendPacket(packet);

		return "1";
	}
}
