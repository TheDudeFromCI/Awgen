package net.whg.we.gamelogic;

/**
 * Represents a game loop.
 *
 * @author TheDudeFromCI
 */
public interface GameLoop
{
	/**
	 * Start the game loop and block until the game loop closes.
	 */
	public void loop();

	/**
	 * Pings the game loop to start shutting down and disposing internal resources.
	 */
	public void requestClose();
}
