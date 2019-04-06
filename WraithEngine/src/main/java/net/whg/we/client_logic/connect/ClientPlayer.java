package net.whg.we.client_logic.connect;

import net.whg.frameworks.network.connect.Player;
import net.whg.frameworks.scene.Transform3D;

/**
 * Represents an online player, from a client's perspective.
 */
public class ClientPlayer implements Player
{
	private String _username;
	private String _token;
	private Transform3D _location;

	public ClientPlayer(String username, String token)
	{
		_username = username;
		_token = token;
		_location = new Transform3D();
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
	public Transform3D getLocation()
	{
		return _location;
	}
}
