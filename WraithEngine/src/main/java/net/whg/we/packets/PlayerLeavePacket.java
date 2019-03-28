package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import net.whg.we.client_logic.connect.ClientPlayer;
import net.whg.we.network.multiplayer.ClientPacketHandler;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

/**
 * This packet is sent when a player joins a server to tell the clients to spawn
 * a local instance of the player and spawn a model at their location.
 */
public class PlayerLeavePacket implements PacketType
{
	public void build(Packet packet, String token)
	{
		packet.getData().put("token", token);
	}

	@Override
	public String getTypePath()
	{
		return "common.player.leave";
	}

	@Override
	public int encode(Packet packet)
	{
		ByteWriter out = packet.getByteWriter();

		out.writeString((String) packet.getData().get("token"), StandardCharsets.UTF_8);

		return out.getPos();
	}

	@Override
	public void decode(Packet packet)
	{
		ByteReader in = packet.getByteReader();
		packet.getData().put("token", in.getString(StandardCharsets.UTF_8));
	}

	@Override
	public void process(Packet packet, PacketHandler handler)
	{
		if (!handler.isClient())
		{
			Log.warn("PlayerLeaveEvent sent to server!");
			return;
		}

		String token = (String) packet.getData().get("token");

		MultiplayerClient multiplayer = ((ClientPacketHandler) handler).getClient();

		ClientPlayer player = (ClientPlayer) multiplayer.getPlayerList().getPlayerByToken(token);
		multiplayer.getPlayerList().removePlayer(player);
	}
}
