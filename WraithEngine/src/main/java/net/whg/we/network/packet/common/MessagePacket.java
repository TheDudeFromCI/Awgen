package net.whg.we.network.packet.common;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketType;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class MessagePacket implements PacketType
{
	public void build(Packet packet, String sender, String message)
	{
		packet.getData().put("sender", sender);
		packet.getData().put("message", message);
	}

	@Override
	public String getTypePath()
	{
		return "common.message";
	}

	@Override
	public int encode(byte[] bytes, Map<String, Object> packetData)
	{
		String sender = (String) packetData.get("sender");
		String message = (String) packetData.get("message");

		ByteWriter writer = new ByteWriter(bytes);
		writer.writeString(sender, StandardCharsets.UTF_16);
		writer.writeString(message, StandardCharsets.UTF_16);

		return writer.getPos();
	}

	@Override
	public void decode(byte[] bytes, int length, Map<String, Object> packetData)
	{
		ByteReader reader = new ByteReader(bytes);

		packetData.put("sender", reader.getString(StandardCharsets.UTF_16));
		packetData.put("message", reader.getString(StandardCharsets.UTF_16));
	}

	@Override
	public void process(Map<String, Object> packetData)
	{
		String sender = (String) packetData.get("sender");
		String message = (String) packetData.get("message");

		Log.infof(">> %s: %s", sender, message);
	}
}
