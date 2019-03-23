package net.whg.we.connect.server;

import java.util.ArrayList;
import net.whg.we.connect.Player;
import net.whg.we.connect.PlayerList;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.multiplayer.OnlinePlayer;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.server.ClientConnection;
import net.whg.we.packets.PlayerJoinPacket;
import net.whg.we.utils.GenericRunnable;
import net.whg.we.utils.logging.Log;

public class ServerPlayerList implements PlayerList
{
    private ArrayList<Player> _players = new ArrayList<>();

    public void addPlayer(ClientConnection client, String username,
            String token)
    {
        OnlinePlayer player = new OnlinePlayer(client, username, token);
        addPlayer(player);

        Log.infof("%s has joined the server.", player.getUsername());
        Log.debugf("%s's token is %s'", player.getUsername(),
                player.getToken());

        // Send location to all players
        {
            forEach(p ->
            {
                if (p == player)
                    return;

                OnlinePlayer p2 = (OnlinePlayer) p;
                Packet packet = p2.newPacket("common.player.join");
                ((PlayerJoinPacket) packet.getPacketType()).build(packet,
                        username, token, player.getLocation());
                p2.sendPacket(packet);
            });
        }

        // Get location of all players
        {
            forEach(p ->
            {
                if (p == player)
                    return;

                Packet packet = player.newPacket("common.player.join");
                ((PlayerJoinPacket) packet.getPacketType()).build(packet,
                        p.getUsername(), p.getToken(), p.getLocation());
                player.sendPacket(packet);
            });
        }
    }

    public OnlinePlayer getPlayerByTCPChannel(TCPChannel channel)
    {
        for (Player player : _players)
            if (((OnlinePlayer) player).getClientConnection()
                    .getTCPChannel() == channel)
                return (OnlinePlayer) player;

        return null;
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
    }

    @Override
    public void removePlayer(Player player)
    {
        if (player == null)
            return;

        _players.remove(player);
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
