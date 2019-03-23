package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.client_logic.connect.ClientPlayer;
import net.whg.we.network.connect.PlayerList;
import net.whg.we.network.multiplayer.ClientPacketHandler;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.network.multiplayer.MultiplayerServer;
import net.whg.we.network.multiplayer.ServerPacketHandler;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.scene.Location;
import net.whg.we.server_logic.connect.OnlinePlayer;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;

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
	public int encode(byte[] bytes, Map<String, Object> packetData)
	{
		ByteWriter out = new ByteWriter(bytes);

		out.writeString((String) packetData.get("token"), StandardCharsets.UTF_8);

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
		String token = (String) packet.getData().get("token");
		Vector3f pos = (Vector3f) packet.getData().get("pos");
		Quaternionf rot = (Quaternionf) packet.getData().get("rot");

		if (handler.isClient())
		{
			MultiplayerClient client = ((ClientPacketHandler) handler).getClient();
			ClientPlayer player = (ClientPlayer) client.getPlayerList().getPlayerByToken(token);

			if (player == null)
				return;

			player.getLocation().setPosition(pos);
			player.getLocation().setRotation(rot);

			player.getCameraSync().sync();
		}
		else
		{
			MultiplayerServer server = ((ServerPacketHandler) handler).getServer();
			PlayerList playerList = server.getPlayerList();
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
