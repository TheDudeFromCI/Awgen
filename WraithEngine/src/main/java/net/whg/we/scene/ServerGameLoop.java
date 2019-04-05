package net.whg.we.scene;

import net.whg.frameworks.logging.Log;
import net.whg.we.utils.Time;

public class ServerGameLoop implements GameLoop
{
	private boolean _running = true;
	private ServerGameState _gameState;
	private boolean _localhost;

	public ServerGameLoop(ServerGameState gameState, boolean localhost)
	{
		_gameState = gameState;
		_localhost = localhost;
	}

	@Override
	public void run()
	{
		Log.info("Starting server game loop.");

		long startTime = System.currentTimeMillis();
		long usedPhysicsFrames = 0;

		boolean clientConnected = false;
		while (_running)
		{
			try
			{
				long currentTime = System.currentTimeMillis();
				double passed = (currentTime - startTime) / 1000.0;
				double physicsFrames = passed * Time.getPhysicsFramerate();

				while (usedPhysicsFrames < physicsFrames)
				{
					usedPhysicsFrames++;
					_gameState.getNetworkHandler().processPackets(_gameState);

					// Sets flag to true when at least one client has connected.
					if (_gameState.getPlayerList().getPlayerCount() > 0)
						clientConnected = true;

					// Closes server when last player has logged off.
					if (clientConnected && _localhost
							&& _gameState.getPlayerList().getPlayerCount() == 0)
					{
						Log.info("Last player has disconnected from server. Closing server.");
						requestClose();
					}
				}

				// Sleep for 1 millisecond to prevent maxing the CPU.
				sleep();
			}
			catch (Exception e)
			{
				Log.fatalf("Uncaught exception in main thread!", e);
				break;
			}
		}

		Log.info("Stopping server game loop.");
		_gameState.getNetworkHandler().stopServer();
		_gameState.getResourceManager().disposeAllResources();
	}

	private void sleep()
	{
		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
		}
	}

	@Override
	public void requestClose()
	{
		Log.debug("Requesting close for server game loop.");
		_running = false;
	}
}
