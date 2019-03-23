package net.whg.we.connect;

import java.util.ArrayList;
import net.whg.we.connect.Player;
import net.whg.we.connect.PlayerList;
import net.whg.we.utils.GenericRunnable;

public class ClientPlayerList implements PlayerList
{
    private ArrayList<Player> _players = new ArrayList<>();

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
    public int getPlayerCount()
    {
        return _players.size();
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

    @Override
    public void forEach(GenericRunnable<Player> action)
    {
        for (Player player : _players)
            action.run(player);
    }
}
