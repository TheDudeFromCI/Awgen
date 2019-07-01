package net.whg.we.client_logic.connect;

import java.util.ArrayList;
import net.whg.frameworks.network.connect.Player;
import net.whg.frameworks.network.connect.PlayerList;
import net.whg.frameworks.util.GenericRunnable;

public class ClientPlayerList implements PlayerList
{
	private ArrayList<Player> _players = new ArrayList<>();
	private ClientPlayer _mainPlayer;

	public ClientPlayerList(String username, String token)
	{
		_mainPlayer = new ClientPlayer(username, token);
	}

	public ClientPlayer getPlayer()
	{
		return _mainPlayer;
	}

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
	public ClientPlayer getPlayerByName(String name)
	{
		for (Player player : _players)
			if (player.getUsername().equals(name))
				return (ClientPlayer) player;

		return null;
	}

	@Override
	public ClientPlayer getPlayerByToken(String token)
	{
		for (Player player : _players)
			if (player.getToken().equals(token))
				return (ClientPlayer) player;

		return null;
	}

	@Override
	public void forEach(GenericRunnable<Player> action)
	{
		for (Player player : _players)
			action.run(player);
	}
}
