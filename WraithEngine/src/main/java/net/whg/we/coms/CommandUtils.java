package net.whg.we.coms;

import net.whg.we.command.CommandList;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.PlayerList;
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
            PlayerList playerList = gameLoop.getServer().getPlayerList();

            commandList.addCommand(new ChatCommand(playerList));
        }
    }
}
