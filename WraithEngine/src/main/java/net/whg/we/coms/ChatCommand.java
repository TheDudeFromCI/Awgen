package net.whg.we.coms;

import net.whg.we.command.Command;
import net.whg.we.command.CommandArgument;
import net.whg.we.command.CommandConsole;
import net.whg.we.command.CommandHandler;
import net.whg.we.connect.PlayerList;
import net.whg.we.connect.server.OnlinePlayer;
import net.whg.we.connect.server.ServerPlayerList;
import net.whg.we.network.packet.Packet;
import net.whg.we.packets.ChatPacket;

public class ChatCommand implements CommandHandler
{
    private static String[] ALIAS = {};

    private ServerPlayerList _playerList;

    public ChatCommand(ServerPlayerList playerList)
    {
        _playerList = playerList;
    }

    @Override
    public String getCommandName()
    {
        return "chat";
    }

    @Override
    public String[] getCommandAliases()
    {
        return ALIAS;
    }

    @Override
    public String getDescription()
    {
        return "Says a phrase in chat.";
    }

    @Override
    public String getHelpText()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("chat <message>\n");
        sb.append(
                "  Broadcasts a chat packet to all users containing the message and username of the sender, "
                        + "with all input parameters being joined, seperated by spaces.\n");

        return sb.toString();
    }

    @Override
    public String executeCommand(Command command)
    {
        CommandArgument[] args = command.getArgs();
        CommandConsole console = command.getCommandSender().getConsole();

        if (args.length == 0)
        {
            console.println("No message specified!");
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++)
        {
            if (i > 0)
                sb.append(' ');
            sb.append(args[i].getValue());
        }
        String message = sb.toString();
        String username = command.getCommandSender().getUsername();

        String out = String.format("%s> %s", username, message);

        _playerList.forEach(player ->
        {
            OnlinePlayer p = (OnlinePlayer) player;

            Packet packet = p.newPacket("common.chat");
            ((ChatPacket) packet.getPacketType()).build(packet, out);
            p.sendPacket(packet);
        });

        return "1";
    }
}
