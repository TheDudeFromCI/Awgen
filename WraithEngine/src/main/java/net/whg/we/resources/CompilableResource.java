package net.whg.we.resources;

import net.whg.we.rendering.Graphics;

/**
 * This interface represents a resource which needs to be compiled by the
 * graphics manager on the main thread.
 *
 * @author TheDudeFromCI
 * @param <T>
 *            The object this resource represents.
 */
public interface CompilableResource extends Resource
{
	/**
	 * Checks if the resource has been compiled or not.
	 *
	 * @return True if the resource has been compiled. False otherwise.
	 */
	public boolean isCompiled();

	/**
	 * Compiles this resource with the current graphics manager. If this resource is
	 * already compiled, nothing happens.
	 *
	 * @param graphics
	 *            - The graphics manager.
	 */
	public void compile(Graphics graphics);
}
