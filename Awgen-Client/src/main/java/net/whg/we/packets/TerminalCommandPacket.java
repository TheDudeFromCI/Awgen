package net.whg.we.packets;

import java.nio.charset.StandardCharsets;
import net.whg.frameworks.command.CommandManager;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.network.connect.Player;
import net.whg.frameworks.network.connect.PlayerList;
import net.whg.frameworks.network.netty.UserConnection;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.packet.PacketHandler;
import net.whg.frameworks.network.packet.PacketType;
import net.whg.frameworks.network.server.OnlinePlayer;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;
import net.whg.we.legacy.ServerGameState;
import net.whg.we.main.GameState;

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
	public int encode(Packet packet)
	{
		ByteWriter out = packet.getByteWriter();

		String command = (String) packet.getData().get("command");
		out.writeString(command, StandardCharsets.UTF_16);

		return out.getPos();
	}

	@Override
	public void decode(Packet packet)
	{
		ByteReader in = packet.getByteReader();

		String command = in.getString(StandardCharsets.UTF_16);

		packet.getData().put("command", command);
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

		GameState gameState = handler.getGameState();
		PlayerList playerList = gameState.getPlayerList();
		Player player = playerList.getPlayerByToken(userConnection.getUserState().getToken());

		Log.infof("Recieved command '%s' from '%s'.", command, player.getUsername());
		Log.debugf("User token: %s", player.getToken());

		CommandManager commandManager = ((ServerGameState) gameState).getCommandManager();
		commandManager.execute(command, ((OnlinePlayer) player).getCommandSender());
	}
}