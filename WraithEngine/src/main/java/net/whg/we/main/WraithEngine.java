package net.whg.we.main;

import org.lwjgl.Version;
import net.whg.we.client_logic.scene.ClientGameState;
import net.whg.we.network.multiplayer.MultiplayerClient;
import net.whg.we.network.multiplayer.MultiplayerServer;
import net.whg.we.network.multiplayer.NetworkManager;
import net.whg.we.scene.ServerGameState;
import net.whg.we.utils.logging.Log;

/**
 * The program entry class. This class is used for the purpose of initializing
 * the plugin loader, all core plugins, and hunting down and launching local
 * plugins.
 *
 * @author TheDudeFromCI
 */
public class WraithEngine
{
	/**
	 * The current version of WraithEngine being run.
	 */
	public static final String VERSION = "v0.1.0a";

	/**
	 * The program entry method.
	 *
	 * @param args
	 *            - Arguments from command line. Used for determining program
	 *            functions. Currently, there are no extra runtime flags that can be
	 *            used at this time.
	 */
	public static void main(String[] args)
	{
		if (!setLogLevel(args))
			return;

		// Log some automatic system info
		Log.trace("Starting WraithEngine.");
		Log.debugf("WraithEngine Version: %s", VERSION);
		Log.debugf("Operating System: %s", System.getProperty("os.name"));
		Log.debugf("Operating System Arch: %s", System.getProperty("os.arch"));
		Log.debugf("Java Version: %s", System.getProperty("java.version"));
		Log.debugf("Java Vendor: %s", System.getProperty("java.vendor"));
		Log.debugf("System User: %s", System.getProperty("user.name"));
		Log.debugf("Working Directory: %s", System.getProperty("user.dir"));
		Log.debugf("LWJGL Version: %s", Version.getVersion());

		NetworkManager networkManager = NetworkManager.parseArgs(args);

		// Start server
		if (networkManager.hasServer())
		{
			MultiplayerServer server = networkManager.getServer();
			Thread thread = new Thread(() ->
			{
				try
				{
					ServerGameState gameState =
							new ServerGameState(server, networkManager.isLocalHost());
					gameState.getGameLoop().run();
				}
				finally
				{
					server.stopServer();
				}
			});
			thread.setDaemon(false);
			thread.setName("server_main");
			thread.start();
		}

		// Start client
		if (networkManager.hasClient())
		{
			MultiplayerClient client = networkManager.getClient();
			Thread thread = new Thread(() ->
			{
				try
				{
					ClientGameState gameState = new ClientGameState(client);
					gameState.getGameLoop().run();
				}
				finally
				{
					networkManager.getClient().stopClient();
				}
			});
			thread.setDaemon(false);
			thread.setName("client_main");
			thread.start();
		}
	}

	private static boolean setLogLevel(String[] args)
	{
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-log"))
			{
				if (args.length <= i + 1)
				{
					System.out.println("Log level not specified!");
					return false;
				}

				int level;
				try
				{
					level = Integer.valueOf(args[i + 1]);
				}
				catch (NumberFormatException e)
				{
					System.out.println("Not a number! '" + args[i + 1] + "'");
					return false;
				}

				if (level < Log.TRACE || level > Log.FATAL)
				{
					System.out.println("Log level must be an interger between " + Log.TRACE
							+ " and " + Log.FATAL + "!");
					return false;
				}

				Log.setLogLevel(level);
				return true;
			}
		}

		Log.setLogLevel(Log.INFO);
		return true;
	}
}
