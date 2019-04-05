package net.whg.frameworks.network.server;

import net.whg.frameworks.network.connect.Player;
import net.whg.frameworks.network.netty.UserConnection;
import net.whg.frameworks.network.packet.Packet;
import net.whg.frameworks.network.packet.PacketManager;
import net.whg.we.scene.Location;
import net.whg.we.scene.Scene;

public class OnlinePlayer implements Player
{
	private UserConnection _userConnection;
	private PlayerCommandSender _commandSender;
	private Location _location;
	private PacketManager _packetManager;
	private Scene _scene;

	public OnlinePlayer(UserConnection client, PacketManager packetManager)
	{
		if (!client.getUserState().isAuthenticated())
			throw new IllegalArgumentException("User Connection not authenticated!");

		_userConnection = client;
		_commandSender = new PlayerCommandSender(this);
		_location = new Location();
		_packetManager = packetManager;

		// TODO Load scene from default location
		_scene = new Scene();
	}

	@Override
	public String getUsername()
	{
		return _userConnection.getUserState().getUsername();
	}

	@Override
	public String getToken()
	{
		return _userConnection.getUserState().getToken();
	}

	public Scene getScene()
	{
		return _scene;
	}

	public void setScene(Scene scene)
	{
		// TODO Send scene change to player
		_scene = scene;
	}

	/**
	 * Sends a packet to this user.
	 *
	 * @param packet
	 *            - The packet to send.
	 */
	public void sendPacket(Packet packet)
	{
		_userConnection.sendPacket(packet);
	}

	public Packet newPacket(String type)
	{
		return _packetManager.newPacket(type);
	}

	/**
	 * Kicks a player from the server.
	 */
	public void kick()
	{
		_userConnection.close();
	}

	public PlayerCommandSender getCommandSender()
	{
		return _commandSender;
	}

	@Override
	public Location getLocation()
	{
		return _location;
	}
}
