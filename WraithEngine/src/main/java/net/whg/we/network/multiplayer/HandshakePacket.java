package net.whg.we.network.multiplayer;

import java.nio.charset.StandardCharsets;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class HandshakePacket implements PacketType
{
	public void build(Packet packet, String username, String token)
	{
		packet.getData().put("username", username);
		packet.getData().put("token", token);
	}

	@Override
	public String getTypePath()
	{
		return "auth.handshake";
	}

	@Override
	public int encode(Packet packet)
	{
		String username = (String) packet.getData().get("username");
		String token = (String) packet.getData().get("token");

		ByteWriter writer = packet.getByteWriter();

		writer.writeString(username, StandardCharsets.UTF_8);
		writer.writeString(token, StandardCharsets.UTF_8);

		return writer.getPos();
	}

	@Override
	public void decode(Packet packet)
	{
		ByteReader reader = packet.getByteReader();

		String username = reader.getString(StandardCharsets.UTF_8);
		String token = reader.getString(StandardCharsets.UTF_8);

		packet.getData().put("username", username);
		packet.getData().put("token", token);
	}

	@Override
	public void process(Packet packet, PacketHandler handler)
	{
		if (handler.isClient())
		{
			Log.warn("Hanshake packed sent from server!");
			return;
		}

		String username = (String) packet.getData().get("username");
		String token = (String) packet.getData().get("token");
		Log.debugf("Recieved handshake packet. Username: %s, Token: %s", username, token);

		packet.getSender().getUserState().authenticate(username, token);

		MultiplayerServer server = ((ServerPacketHandler) handler).getServer();
		server.getPlayerList().addPlayer(packet.getSender());
	}
}
