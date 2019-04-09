package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.network.netty.UserConnection;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.packet.PacketHandler;
import net.whg.frameworks.network.packet.PacketType;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class ImportRequestPacket implements PacketType
{
	public static void build(Packet packet, String filepath)
	{
		// TODO Full file paths should *not* be sent to the server. Only file name and
		// file type. Seriously, fix this.

		/*
		 * The correct way to do this would be to store the filepath on the client in a
		 * hash map with a random number as a key. Send that key to the server, along
		 * with the file name and extension. If the server approves, that key is
		 * returned and the client will pull that id out of the list and start uploading
		 * the local file.
		 */

		packet.getData().put("file", filepath);
	}

	@Override

	public String getTypePath()
	{
		return "player.import_resource";
	}

	@Override
	public int encode(Packet packet)
	{
		ByteWriter out = packet.getByteWriter();

		String file = (String) packet.getData().get("file");
		out.writeString(file, StandardCharsets.UTF_8);

		return out.getPos();
	}

	@Override
	public void decode(Packet packet)
	{
		ByteReader in = packet.getByteReader();

		String file = in.getString(StandardCharsets.UTF_8);
		packet.getData().put("file", file);
	}

	@Override
	public void process(Packet packet, PacketHandler handler)
	{
		if (!handler.isClient())
		{
			UserConnection user = packet.getSender();
			Log.warnf("Recived import request packet from client %s! {Token %s}",
					user.getUserState().getUsername(), user.getUserState().getToken());
			return;
		}

		// TODO Start uploading file.
	}
}
