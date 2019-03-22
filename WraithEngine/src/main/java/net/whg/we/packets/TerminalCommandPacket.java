package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.whg.we.command.CommandManager;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.OnlinePlayer;
import net.whg.we.network.multiplayer.PlayerList;
import net.whg.we.network.multiplayer.ServerPacketHandler;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class TerminalCommandPacket implements PacketType
{
    public void build(Packet packet, String command)
    {
        packet.getData().put("command", command);
    }

    @Override
    public String getTypePath()
    {
        return "common.terminal.out";
    }

    @Override
    public int encode(byte[] bytes, Map<String, Object> packetData)
    {
        ByteWriter out = new ByteWriter(bytes);

        String command = (String) packetData.get("command");
        out.writeString(command, StandardCharsets.UTF_16);

        return out.getPos();
    }

    @Override
    public void decode(byte[] bytes, int length, Map<String, Object> packetData)
    {
        ByteReader in = new ByteReader(bytes);

        String command = in.getString(StandardCharsets.UTF_16);

        packetData.put("command", command);
    }

    @Override
    public void process(Packet packet, PacketHandler handler)
    {
        String command = (String) packet.getData().get("command");

        if (handler.isClient())
        {
            Log.infof("Recieved command '%s' from server.", command);

            // TODO Commands should be able to be parsed on the client, too.
            Log.warnf("Command recieved from server! '%s'", command);
        }
        else
        {
            ServerPacketHandler s_handler = (ServerPacketHandler) handler;
            PlayerList playerList = s_handler.getServer().getPlayerList();
            OnlinePlayer player =
                    playerList.getPlayerByTCPChannel(packet.getSender());

            Log.infof("Recieved command '%s' from '%s'.", command,
                    player.getUsername());
            Log.debugf("User token: %s", player.getUserToken());

            GameState gameState = handler.getGameState();
            CommandManager commandManager = gameState.getCommandManager();
            commandManager.execute(command, player.getCommandSender());
        }
    }
}
