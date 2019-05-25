package net.whg.frameworks.network.connect;

import net.whg.frameworks.util.GenericRunnable;

public interface PlayerList
{
    void addPlayer(Player player);

    void removePlayer(Player player);

    int getPlayerCount();

    Player getPlayerByName(String name);

    Player getPlayerByToken(String token);

    void forEach(GenericRunnable<Player> action);
}