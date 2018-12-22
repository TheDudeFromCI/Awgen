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
	 * If this game loop is currently managing a window, it should request the
	 * window to be closed as well.
	 */
	public void requestClose();
}
