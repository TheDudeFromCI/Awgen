package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;
import net.whg.we.client_logic.connect.ClientPlayer;
import net.whg.we.client_logic.connect.ClientPlayerList;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.scene.Location;
import net.whg.we.utils.logging.Log;

/**
 * This packet is sent when a player joins a server to tell the clients to spawn
 * a local instance of the player and spawn a model at their location.
 */
public class PlayerJoinPacket implements PacketType
{
	public void build(Packet packet, String username, String token, Location location)
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
	public int encode(Packet packet)
	{
		ByteWriter out = packet.getByteWriter();

		out.writeString((String) packet.getData().get("username"), StandardCharsets.UTF_8);
		out.writeString((String) packet.getData().get("token"), StandardCharsets.UTF_8);

		Vector3f pos = (Vector3f) packet.getData().get("pos");
		out.writeFloat(pos.x);
		out.writeFloat(pos.y);
		out.writeFloat(pos.z);

		Quaternionf quat = (Quaternionf) packet.getData().get("rot");
		out.writeFloat(quat.x);
		out.writeFloat(quat.y);
		out.writeFloat(quat.z);
		out.writeFloat(quat.w);

		return out.getPos();
	}

	@Override
	public void decode(Packet packet)
	{
		ByteReader in = packet.getByteReader();

		packet.getData().put("username", in.getString(StandardCharsets.UTF_8));
		packet.getData().put("token", in.getString(StandardCharsets.UTF_8));

		Vector3f pos = new Vector3f();
		pos.x = in.getFloat();
		pos.y = in.getFloat();
		pos.z = in.getFloat();
		packet.getData().put("pos", pos);

		Quaternionf quat = new Quaternionf();
		quat.x = in.getFloat();
		quat.y = in.getFloat();
		quat.z = in.getFloat();
		quat.w = in.getFloat();
		packet.getData().put("rot", quat);
	}

	@Override
	public void process(Packet packet, PacketHandler handler)
	{
		if (!handler.isClient())
		{
			Log.warn("PlayerJoinEvent sent to server!");
			return;
		}

		String username = (String) packet.getData().get("username");
		String token = (String) packet.getData().get("token");

		ClientPlayerList playerList = (ClientPlayerList) handler.getGameState().getPlayerList();
		ClientPlayer player = new ClientPlayer(username, token);
		playerList.addPlayer(player);

		player.getLocation().setPosition((Vector3f) packet.getData().get("pos"));
		player.getLocation().setRotation((Quaternionf) packet.getData().get("rot"));

		// TODO Build model for player
	}
}
