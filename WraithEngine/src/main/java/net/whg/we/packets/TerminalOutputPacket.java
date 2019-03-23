package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.ServerPacketHandler;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.scene.WindowedGameLoop;
import net.whg.we.server_logic.connect.OnlinePlayer;
import net.whg.we.server_logic.connect.ServerPlayerList;
import net.whg.we.ui.terminal.ConsoleOutput;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class TerminalOutputPacket implements PacketType
{
    private static final int SET_LINE_TYPE = 0;
    private static final int SCROLL_POS_TYPE = 1;

    public void build_SetLine(Packet packet, int lineIndex, String text)
    {
        packet.getData().put("type", SET_LINE_TYPE);
        packet.getData().put("line_index", lineIndex);
        packet.getData().put("text", text);
    }

    public void build_ScollPos(Packet packet, int scrollPos)
    {
        packet.getData().put("type", SCROLL_POS_TYPE);
        packet.getData().put("pos", scrollPos);
    }

    @Override
    public String getTypePath()
    {
        return "common.terminal.in";
    }

    @Override
    public int encode(byte[] bytes, Map<String, Object> packetData)
    {
        ByteWriter out = new ByteWriter(bytes);

        int type = (int) packetData.get("type");
        out.writeByte(type);

        if (type == SET_LINE_TYPE)
        {
            int lineIndex = (int) packetData.get("line_index");
            String text = (String) packetData.get("text");

            out.writeInt(lineIndex);
            out.writeString(text, StandardCharsets.UTF_16);
        }
        else
        {
            int scrollPos = (int) packetData.get("pos");
            out.writeInt(scrollPos);
        }

        return out.getPos();
    }

    @Override
    public void decode(byte[] bytes, int length, Map<String, Object> packetData)
    {
        ByteReader in = new ByteReader(bytes);

        int type = in.getByte();
        packetData.put("type", type);

        if (type == SET_LINE_TYPE)
        {
            int lineIndex = in.getInt();
            String text = in.getString(StandardCharsets.UTF_16);

            packetData.put("line_index", lineIndex);
            packetData.put("text", text);
        }
        else
        {
            int scrollPos = in.getInt();
            packetData.put("pos", scrollPos);
        }
    }

    @Override
    public void process(Packet packet, PacketHandler handler)
    {
        if (handler.isClient())
        {
            GameState gameState = handler.getGameState();
            WindowedGameLoop gameLoop =
                    (WindowedGameLoop) gameState.getGameLoop();
            ConsoleOutput console = gameLoop.getTerminal().getConsoleOutput();

            int type = (int) packet.getData().get("type");

            if (type == SET_LINE_TYPE)
            {
                int line = (int) packet.getData().get("line_index");
                String text = (String) packet.getData().get("text");

                Log.tracef("Setting console line %d to '%s'.", line, text);

                console.setLine(line, text);
            }
            else
            {
                int scrollPos = (int) packet.getData().get("pos");
                console.setScroll(scrollPos);
            }
        }
        else
        {
            // TODO Can... we send packets to the client to execute??

            ServerPacketHandler s_handler = (ServerPacketHandler) handler;
            ServerPlayerList playerList = s_handler.getServer().getPlayerList();
            OnlinePlayer player =
                    playerList.getPlayerByTCPChannel(packet.getSender());

            Log.warnf("Command output recieved from client %s",
                    player.getUsername());
            Log.debugf("User token: %s", player.getToken());
        }
    }
}
