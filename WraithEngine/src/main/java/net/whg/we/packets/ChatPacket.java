package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.scene.WindowedGameLoop;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class ChatPacket implements PacketType
{
    public void build(Packet packet, String output)
    {
        packet.getData().put("message", output);
    }

    @Override
    public String getTypePath()
    {
        return "common.chat";
    }

    @Override
    public int encode(byte[] bytes, Map<String, Object> packetData)
    {
        ByteWriter out = new ByteWriter(bytes);

        String message = (String) packetData.get("message");
        out.writeString(message, StandardCharsets.UTF_16);

        return out.getPos();
    }

    @Override
    public void decode(byte[] bytes, int length, Map<String, Object> packetData)
    {
        ByteReader in = new ByteReader(bytes);

        String message = in.getString(StandardCharsets.UTF_16);
        packetData.put("message", message);
    }

    @Override
    public void process(Packet packet, PacketHandler handler)
    {
        if (handler.isClient())
        {
            String message = (String) packet.getData().get("message");
            Log.info(message);

            // TODO Once a chat window is implemented, send it there instead

            MultiplayerClient client =
                    ((WindowedGameLoop) handler.getGameState().getGameLoop())
                            .getClient();
            Packet p2 = client.newPacket("common.terminal.out");
            ((TerminalCommandPacket) p2.getPacketType()).build(p2,
                    String.format("print \"%s\"", message));
            client.sendPacket(p2);
        }
        else
        {
            // TODO Packet is send from the chat window. Handle accordingly.
        }
    }
}
