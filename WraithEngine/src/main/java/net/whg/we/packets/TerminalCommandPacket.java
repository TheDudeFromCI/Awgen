package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.whg.we.main.GameState;
import net.whg.we.network.multiplayer.ServerPacketHandler;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketHandler;
import net.whg.we.network.packet.PacketType;
import net.whg.we.server_logic.command.CommandManager;
import net.whg.we.server_logic.connect.OnlinePlayer;
import net.whg.we.server_logic.connect.ServerPlayerList;
import net.whg.we.utils.ByteReader;
import net.whg.we.utils.ByteWriter;
import net.whg.we.utils.logging.Log;

public class TerminalCommandPacket implements PacketType
{
	public void build(Packet packet, String command)
	{
		packet.getData().put("command", command);
	}

	@Override
	public String getTypePath()
	{
		return "common.terminal.out";
	}

	@Override
	public int encode(byte[] bytes, Map<String, Object> packetData)
	{
		ByteWriter out = new ByteWriter(bytes);

		String command = (String) packetData.get("command");
		out.writeString(command, StandardCharsets.UTF_16);

		return out.getPos();
	}

	@Override
	public void decode(byte[] bytes, int length, Map<String, Object> packetData)
	{
		ByteReader in = new ByteReader(bytes);

		String command = in.getString(StandardCharsets.UTF_16);

		packetData.put("command", command);
	}

	@Override
	public void process(Packet packet, PacketHandler handler)
	{
		String command = (String) packet.getData().get("command");

		if (handler.isClient())
		{
			Log.warnf("Command recieved from server! '%s'", command);
			return;
		}

		UserConnection userConnection = packet.getSender();

		if (!userConnection.getUserState().isAuthenticated())
		{
			Log.warnf("Client has attempted to send packet before authentication! IP: %s",
					userConnection.getIP());
			return;
		}

		ServerPacketHandler s_handler = (ServerPacketHandler) handler;
		ServerPlayerList playerList = s_handler.getServer().getPlayerList();
		OnlinePlayer player = (OnlinePlayer) playerList
				.getPlayerByToken(userConnection.getUserState().getToken());

		Log.infof("Recieved command '%s' from '%s'.", command, player.getUsername());
		Log.debugf("User token: %s", player.getToken());

		GameState gameState = handler.getGameState();
		CommandManager commandManager = gameState.getCommandManager();
		commandManager.execute(command, player.getCommandSender());
	}
}
