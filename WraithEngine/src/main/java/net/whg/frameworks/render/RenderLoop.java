package net.whg.frameworks.render;

/**
 * Represents a class which can be used to handling rendering and update loops
 * within the game.
 *
 * @author TheDudeFromCI
 */
public interface RenderLoop
{
	/**
	 * Starts the game loop. This loop with continue running until the game exits or
	 * is force class. This method is blocking.
	 */
	void loop();
}
