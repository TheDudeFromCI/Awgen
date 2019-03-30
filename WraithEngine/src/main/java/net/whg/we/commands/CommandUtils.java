package net.whg.we.commands;

import net.whg.we.command.CommandList;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.ServerGameLoop;
import net.whg.we.network.server.ServerPlayerList;

public class CommandUtils
{
	public static void addAdvancedCommands(CommandList commandList, GameState gameState)
	{
		if (gameState.isClient())
			throw new IllegalStateException("Commands only can be run from the server!");

		ServerGameLoop gameLoop = (ServerGameLoop) gameState.getGameLoop();
		ServerPlayerList playerList = gameLoop.getServer().getPlayerList();

		commandList.addCommand(new ChatCommand(playerList));
		commandList.addCommand(new SceneHierarchyCommand());
	}
}
