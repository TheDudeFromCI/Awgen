package net.whg.we.client_logic.connect;

import net.whg.we.network.connect.Player;
import net.whg.we.utils.Location;

/**
 * Represents an online player, from a client's perspective.
 */
public class ClientPlayer implements Player
{
    private String _username;
    private String _token;
    private Location _location;

    public ClientPlayer(String username, String token)
    {
        _username = username;
        _token = token;
        _location = new Location();
    }

    @Override
    public String getUsername()
    {
        return _username;
    }

    @Override
    public String getToken()
    {
        return _token;
    }

    @Override
    public Location getLocation()
    {
        return _location;
    }
}
