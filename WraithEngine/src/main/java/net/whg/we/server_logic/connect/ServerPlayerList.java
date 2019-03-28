package net.whg.we.server_logic.connect;

import java.util.ArrayList;
import net.whg.we.network.connect.Player;
import net.whg.we.network.connect.PlayerList;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketManager;
import net.whg.we.packets.PlayerJoinPacket;
import net.whg.we.packets.PlayerLeavePacket;
import net.whg.we.utils.GenericRunnable;
import net.whg.we.utils.logging.Log;

public class ServerPlayerList implements PlayerList
{
	private ArrayList<Player> _players = new ArrayList<>();
	private PacketManager _packetManager;

	public ServerPlayerList(PacketManager packetManager)
	{
		_packetManager = packetManager;
	}

	public void addPlayer(UserConnection client)
	{
		OnlinePlayer player = new OnlinePlayer(client, _packetManager);
		addPlayer(player);
	}

	@Override
	public void forEach(GenericRunnable<Player> action)
	{
		for (Player player : _players)
			action.run(player);
	}

	public void clear()
	{
		_players.clear();
	}

	@Override
	public int getPlayerCount()
	{
		return _players.size();
	}

	@Override
	public void addPlayer(Player player)
	{
		if (player == null)
			return;

		if (_players.contains(player))
			return;

		_players.add(player);

		Log.infof("%s has joined the server.", player.getUsername());
		Log.debugf("%s's token is %s'", player.getUsername(), player.getToken());

		// Send location to all players
		{
			forEach(p ->
			{
				if (p == player)
					return;

				OnlinePlayer p2 = (OnlinePlayer) p;
				Packet packet = p2.newPacket("common.player.join");
				((PlayerJoinPacket) packet.getPacketType()).build(packet, player.getUsername(),
						player.getToken(), player.getLocation());
				p2.sendPacket(packet);
			});
		}

		// Get location of all players
		OnlinePlayer onlinePlayer = (OnlinePlayer) player;
		{
			forEach(p ->
			{
				if (p == player)
					return;

				Packet packet = onlinePlayer.newPacket("common.player.join");
				((PlayerJoinPacket) packet.getPacketType()).build(packet, p.getUsername(),
						p.getToken(), p.getLocation());
				onlinePlayer.sendPacket(packet);
			});
		}
	}

	@Override
	public void removePlayer(Player player)
	{
		if (player == null)
			return;

		_players.remove(player);

		// Send disconnect to all players
		{
			forEach(p ->
			{
				if (p == player)
					return;

				OnlinePlayer p2 = (OnlinePlayer) p;
				Packet packet = p2.newPacket("common.player.leave");
				((PlayerLeavePacket) packet.getPacketType()).build(packet, player.getToken());
				p2.sendPacket(packet);
			});
		}
	}

	@Override
	public Player getPlayerByName(String name)
	{
		for (Player player : _players)
			if (player.getUsername().equals(name))
				return player;

		return null;
	}

	@Override
	public Player getPlayerByToken(String token)
	{
		for (Player player : _players)
			if (player.getToken().equals(token))
				return player;

		return null;
	}
}
