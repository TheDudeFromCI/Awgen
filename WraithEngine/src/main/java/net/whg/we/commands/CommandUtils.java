package net.whg.we.commands;

import net.whg.frameworks.command.CommandList;
import net.whg.we.legacy.ServerGameState;

public class CommandUtils
{
	public static void addAdvancedCommands(CommandList commandList, ServerGameState gameState)
	{
		commandList.addCommand(new ChatCommand(gameState.getPlayerList()));
		commandList.addCommand(new SceneHierarchyCommand());
	}
}
