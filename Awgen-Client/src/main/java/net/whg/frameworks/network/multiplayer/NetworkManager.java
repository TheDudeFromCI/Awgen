package net.whg.frameworks.network.multiplayer;

import java.io.IOException;
import java.util.UUID;
import net.whg.frameworks.logging.Log;

public class NetworkManager
{
	public static NetworkManager parseArgs(String[] args)
	{
		NetworkManager networkManager = new NetworkManager();

		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-s") || args[i].equals("-server"))
			{
				if (args.length <= i + 1)
					throw new IllegalArgumentException("Port not specified for server!");

				i++;
				int port;

				try
				{
					port = Integer.valueOf(args[i]);
				}
				catch (NumberFormatException e)
				{
					throw new IllegalArgumentException("Not a number! '" + args[i] + "'");
				}

				try
				{
					networkManager.attachServer(port);
				}
				catch (IOException e)
				{
					Log.errorf("Failed to start server!", e);
				}

			}

			if (args[i].equals("-c") || args[i].equals("-client"))
			{
				if (args.length <= i + 1)
					throw new IllegalArgumentException("IP not specified for client!");
				if (args.length <= i + 2)
					throw new IllegalArgumentException("Username not specified for client!");

				i++;

				String[] ip_parts = args[i].split(":");

				String ip = ip_parts[0];
				int port;

				try
				{
					port = Integer.valueOf(ip_parts[1]);
				}
				catch (NumberFormatException e)
				{
					throw new IllegalArgumentException("Not a number! '" + args[i] + "'");
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					throw new IllegalArgumentException("Port not specified! '" + args[i] + "'");
				}

				i++;
				String username = args[i];

				try
				{
					networkManager.attachClient(username, ip, port);
				}
				catch (IOException e)
				{
					Log.errorf("Failed to start server!", e);
				}

			}
		}

		return networkManager;
	}

	private MultiplayerServer _server;
	private MultiplayerClient _client;

	public void attachServer(int port) throws IOException
	{
		if (hasServer())
			throw new IllegalStateException("Server already attached!");

		_server = new MultiplayerServer();
		_server.startServer(port);
	}

	private String randomToken()
	{
		return UUID.randomUUID().toString();
	}

	public void attachClient(String username, String ip, int port) throws IOException
	{
		if (hasClient())
			throw new IllegalStateException("Client already attached!");

		_client = new MultiplayerClient(username, randomToken());
		_client.startClient(ip, port);
	}

	public boolean hasServer()
	{
		return _server != null;
	}

	public boolean hasClient()
	{
		return _client != null;
	}

	public MultiplayerServer getServer()
	{
		return _server;
	}

	public MultiplayerClient getClient()
	{
		return _client;
	}

	public boolean isLocalHost()
	{
		return hasServer() && hasClient();
	}
}
