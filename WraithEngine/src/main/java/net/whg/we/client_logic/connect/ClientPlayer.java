package net.whg.we.client_logic.connect;

import net.whg.we.client_logic.utils.PlayerCameraModelSync;
import net.whg.we.network.connect.Player;
import net.whg.we.scene.Location;

/**
 * Represents an online player, from a client's perspective.
 */
public class ClientPlayer implements Player
{
	private String _username;
	private String _token;
	private Location _location;
	private PlayerCameraModelSync _cameraModelSync;

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

	public PlayerCameraModelSync getCameraSync()
	{
		return _cameraModelSync;
	}

	public void setCameraModelSync(PlayerCameraModelSync sync)
	{
		_cameraModelSync = sync;
	}
}