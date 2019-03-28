package net.whg.we.server_logic.connect;

import net.whg.we.network.connect.Player;
import net.whg.we.network.netty.UserConnection;
import net.whg.we.network.packet.Packet;
import net.whg.we.network.packet.PacketManager;
import net.whg.we.scene.Location;

public class OnlinePlayer implements Player
{
	private UserConnection _userConnection;
	private PlayerCommandSender _commandSender;
	private Location _location;
	private PacketManager _packetManager;

	public OnlinePlayer(UserConnection client, PacketManager packetManager)
	{
		if (!client.getUserState().isAuthenticated())
			throw new IllegalArgumentException("User Connection not authenticated!");

		_userConnection = client;
		_commandSender = new PlayerCommandSender(this);
		_location = new Location();
		_packetManager = packetManager;
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
