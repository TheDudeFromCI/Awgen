package net.whg.frameworks.network.multiplayer;

import java.nio.charset.StandardCharsets;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.packet.PacketHandler;
import net.whg.frameworks.network.packet.PacketType;
import net.whg.frameworks.network.server.ServerPlayerList;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

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

		((ServerPlayerList) handler.getGameState().getPlayerList()).addPlayer(packet.getSender());
	}
}