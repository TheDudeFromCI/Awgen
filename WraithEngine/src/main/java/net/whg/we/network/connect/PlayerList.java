package net.whg.we.network.connect;

import net.whg.we.utils.GenericRunnable;

public interface PlayerList
{
    void addPlayer(Player player);

    void removePlayer(Player player);

    int getPlayerCount();

    Player getPlayerByName(String name);

    Player getPlayerByToken(String token);

    void forEach(GenericRunnable<Player> action);
}
