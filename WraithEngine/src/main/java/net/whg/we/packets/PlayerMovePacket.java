package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.frameworks.network.connect.PlayerList;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.packet.PacketHandler;
import net.whg.frameworks.network.packet.PacketType;
import net.whg.frameworks.network.server.OnlinePlayer;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;
import net.whg.we.legacy.Location;

/**
 * This packet is sent when a player joins a server to tell the clients to spawn
 * a local instance of the player and spawn a model at their location.
 */
public class PlayerMovePacket implements PacketType
{
	public void build(Packet packet, String token, Location location)
	{
		packet.getData().put("token", token);
		packet.getData().put("pos", location.getPosition());
		packet.getData().put("rot", location.getRotation());
	}

	@Override
	public String getTypePath()
	{
		return "common.player.move";
	}

	@Override
	public int encode(Packet packet)
	{
		ByteWriter out = packet.getByteWriter();

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
		String token = (String) packet.getData().get("token");
		Vector3f pos = (Vector3f) packet.getData().get("pos");
		Quaternionf rot = (Quaternionf) packet.getData().get("rot");

		if (handler.isClient())
		{
			// TODO Find player model and move it
		}
		else
		{
			PlayerList playerList = handler.getGameState().getPlayerList();
			OnlinePlayer player = (OnlinePlayer) playerList.getPlayerByToken(token);

			if (player == null)
				return;

			player.getLocation().setPosition(pos);
			player.getLocation().setRotation(rot);

			playerList.forEach(p ->
			{
				if (p == player)
					return;

				Packet movePacket = ((OnlinePlayer) p).newPacket("common.player.move");
				((PlayerMovePacket) movePacket.getPacketType()).build(movePacket, token,
						player.getLocation());
				((OnlinePlayer) p).sendPacket(movePacket);
			});
		}
	}
}
