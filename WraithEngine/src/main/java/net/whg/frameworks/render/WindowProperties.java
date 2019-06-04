package net.whg.frameworks.render;

/**
 * The set of properties used to build the game window.
 *
 * @author TheDudeFromCI
 */
public class WindowProperties implements Cloneable
{
	/**
	 * The title of the window.
	 */
	public String title = "Untitled Window";

	/**
	 * The width of the window.
	 */
	public int width = 640;

	/**
	 * The height of the window.
	 */
	public int height = 480;

	/**
	 * Creates a copy of this window properties object.
	 * 
	 * @return A copy of this window properties object.
	 */
	public WindowProperties copy()
	{
		try
		{
			return (WindowProperties) clone();
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}
}
