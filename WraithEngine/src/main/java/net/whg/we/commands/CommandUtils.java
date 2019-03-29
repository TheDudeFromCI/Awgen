package net.whg.we.commands;

import net.whg.we.command.CommandList;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.ServerGameLoop;
import net.whg.we.network.server.ServerPlayerList;

public class CommandUtils
{
    public static void addAdvancedCommands(CommandList commandList,
            GameState gameState)
    {
        if (gameState.isClient())
        {
            commandList.addCommand(new ChatCommand(null));
        }
        else
        {
            ServerGameLoop gameLoop = (ServerGameLoop) gameState.getGameLoop();
            ServerPlayerList playerList = gameLoop.getServer().getPlayerList();

            commandList.addCommand(new ChatCommand(playerList));
        }
    }
}
