package net.whg.we.network.multiplayer;

import java.util.ArrayList;
import org.joml.Quaternionf;
import net.whg.we.network.TCPChannel;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.server.ClientConnection;
import net.whg.we.packets.PlayerJoinPacket;
import net.whg.we.utils.GenericRunnable;
import net.whg.we.utils.Location;
import net.whg.we.utils.logging.Log;

public class PlayerList
{
    private ArrayList<OnlinePlayer> _players = new ArrayList<>();

    void addPlayer(ClientConnection client, String username, String token)
    {
        OnlinePlayer player = new OnlinePlayer(client, username, token);
        _players.add(player);

        Log.infof("%s has joined the server.", player.getUsername());
        Log.debugf("%s's token is %s'", player.getUsername(),
                player.getUserToken());

        // Send location to all players
        {
            forEach(p ->
            {
                if (p == player)
                    return;

                Packet packet = p.newPacket("common.player.join");
                ((PlayerJoinPacket) packet.getPacketType()).build(packet,
                        username, token, player.getLocation());
                p.sendPacket(packet);
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
                        p.getUsername(), p.getUserToken(), p.getLocation());
                player.sendPacket(packet);
            });
        }
    }

    void removePlayer(ClientConnection client)
    {
        for (int i = 0; i < _players.size(); i++)
            if (_players.get(i).getClientConnection() == client)
            {
                OnlinePlayer player = _players.get(i);
                Log.infof("%s has left the server.", player.getUsername());
                Log.debugf("%s's token is %s'", player.getUsername(),
                        player.getUserToken());

                _players.remove(i);
                return;
            }
    }

    public OnlinePlayer getPlayerByName(String name)
    {
        for (OnlinePlayer player : _players)
            if (player.getUsername().equals(name))
                return player;

        return null;
    }

    public OnlinePlayer getPlayerByToken(String token)
    {
        for (OnlinePlayer player : _players)
            if (player.getUserToken().equals(token))
                return player;

        return null;
    }

    public OnlinePlayer getPlayerByTCPChannel(TCPChannel channel)
    {
        for (OnlinePlayer player : _players)
            if (player.getClientConnection().getTCPChannel() == channel)
                return player;

        return null;
    }

    public void forEach(GenericRunnable<OnlinePlayer> action)
    {
        for (OnlinePlayer player : _players)
            action.run(player);
    }

    public void clear()
    {
        _players.clear();
    }

    public int getPlayerCount()
    {
        return _players.size();
    }
}
