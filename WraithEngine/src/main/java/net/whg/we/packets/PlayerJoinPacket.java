package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.resources.scene.ModelResource;
import net.whg.we.scene.GameObject;
import net.whg.we.scene.Model;
import net.whg.we.scene.WindowedGameLoop;
import net.whg.we.scene.behaviours.RenderBehaviour;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.Location;
import net.whg.we.utils.logging.Log;

/**
 * This packet is sent when a player joins a server to tell the clients to spawn
 * a local instance of the player and spawn a model at their location.
 */
public class PlayerJoinPacket implements PacketType
{
    public void build(Packet packet, String username, String token,
            Location location)
    {
        packet.getData().put("username", username);
        packet.getData().put("token", token);
        packet.getData().put("pos", location.getPosition());
        packet.getData().put("rot", location.getRotation());
    }

    @Override
    public String getTypePath()
    {
        return "common.player.join";
    }

    @Override
    public int encode(byte[] bytes, Map<String, Object> packetData)
    {
        ByteWriter out = new ByteWriter(bytes);

        out.writeString((String) packetData.get("username"),
                StandardCharsets.UTF_8);
        out.writeString((String) packetData.get("token"),
                StandardCharsets.UTF_8);

        Vector3f pos = (Vector3f) packetData.get("pos");
        out.writeFloat(pos.x);
        out.writeFloat(pos.y);
        out.writeFloat(pos.z);

        Quaternionf quat = (Quaternionf) packetData.get("rot");
        out.writeFloat(quat.x);
        out.writeFloat(quat.y);
        out.writeFloat(quat.z);
        out.writeFloat(quat.w);

        return out.getPos();
    }

    @Override
    public void decode(byte[] bytes, int length, Map<String, Object> packetData)
    {
        ByteReader in = new ByteReader(bytes);

        packetData.put("username", in.getString(StandardCharsets.UTF_8));
        packetData.put("token", in.getString(StandardCharsets.UTF_8));

        Vector3f pos = new Vector3f();
        pos.x = in.getFloat();
        pos.y = in.getFloat();
        pos.z = in.getFloat();
        packetData.put("pos", pos);

        Quaternionf quat = new Quaternionf();
        quat.x = in.getFloat();
        quat.y = in.getFloat();
        quat.z = in.getFloat();
        quat.w = in.getFloat();
        packetData.put("rot", quat);
    }

    @Override
    public void process(Packet packet, PacketHandler handler)
    {
        if (!handler.isClient())
        {
            Log.warn("PlayerJoinEvent sent to server!");
            return;
        }

        WindowedGameLoop gameLoop =
                (WindowedGameLoop) handler.getGameState().getGameLoop();
        {
            ModelResource terrain =
                    (ModelResource) gameLoop.getResourceManager().loadResource(
                            gameLoop.getCorePlugin(), "models/human.model");
            terrain.compile(gameLoop.getGraphicsPipeline().getGraphics());
            Model model = terrain.getData();
            model.getLocation()
                    .setPosition((Vector3f) packet.getData().get("pos"));
            model.getLocation()
                    .setRotation((Quaternionf) packet.getData().get("rot"));
            GameObject go =
                    gameLoop.getScene().getGameObjectManager().createNew();
            go.addBehaviour(new RenderBehaviour(model));
        }
    }
}
