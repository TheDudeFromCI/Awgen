package net.whg.frameworks.render;

/**
 * Represents the graphics manager, used for handling all rendering and graphic
 * resource bindings.
 *
 * @author TheDudeFromCI
 */
public class GraphicsManager
{
	private Window window;

	/**
	 * Assigns a window handler to this graphics manager. If a window already exists
	 * within this graphics manager and is open, it is destroyed and replaced.
	 * 
	 * @param window
	 *     - The new window to assign to this graphics manager.
	 */
	public void setWindow(Window window)
	{
		if (this.window != null && this.window.isOpen())
			this.window.destory();

		this.window = window;
	}

	/**
	 * Gets the current window assigned to this graphics manager, or null if no
	 * window has yet been assigned.
	 * 
	 * @return The window currently assigned to this graphics manager.
	 */
	public Window getWindow()
	{
		return window;
	}
}
