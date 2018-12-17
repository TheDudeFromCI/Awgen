package net.whg.we.utils;

/**
 * A utility class for talking to the screen.
 *
 * @author TheDudeFromCI
 */
public class Screen
{
	private static int _width = 640;
	private static int _height = 480;

	/**
	 * Changes the internal referenced size of the screen. This should only be
	 * called by the window listener when a window is resized.
	 *
	 * @param width
	 *            - The new width of the screen.
	 * @param height
	 *            - The new height of the screen.
	 */
	public static void updateSize(int width, int height)
	{
		_width = width;
		_height = height;
	}

	/**
	 * Gets the current width of the screen in pixels.
	 * 
	 * @return The width of the screen.
	 */
	public static int width()
	{
		return _width;
	}

	/**
	 * Gets the current height of the screen in pixels.
	 * 
	 * @return The height of the screen.
	 */
	public static int height()
	{
		return _height;
	}

	/**
	 * Gets the current aspect ratio of the screen.
	 * 
	 * @return The aspect ration of the screen.
	 */
	public static float aspect()
	{
		return (float) _width / _height;
	}
}
