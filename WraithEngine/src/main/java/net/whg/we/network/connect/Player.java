package net.whg.we.network.connect;

import net.whg.we.utils.Location;

public interface Player
{
    /**
     * Gets the username (display name) for this player. This value should not
     * change while the player is connected to a server, but may be changed
     * while the player is offline. While each player must have a unquie
     * username, this username is not promised to be static. Old usernames may
     * be claimed by other players.
     * 
     * @return The current username for this player.
     */
    String getUsername();

    /**
     * Gets the user token for this player. A user token is a random string of
     * numbers and letters that is completely unquie to this account. This value
     * is permanitely linked to this account and can safely be used to determine
     * different players in configuration files while allowing them to freely
     * change their usernames at any time. Tokens remain persistant amoung
     * deleted accounts.
     * 
     * @return The token for this user account.
     */
    String getToken();

    Location getLocation();
}
