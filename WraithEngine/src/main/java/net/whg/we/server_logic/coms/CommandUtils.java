package net.whg.we.server_logic.coms;

import net.whg.we.server_logic.command.CommandList;
import net.whg.we.server_logic.connect.ServerPlayerList;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.ServerGameLoop;

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