package net.whg.frameworks.event;

public interface Listener
{
	/**
	 * Gets the current priority level of the listener relative to other listeners.
	 * Listeners are called in order of the priority level, with smaller numbers
	 * being called first and larger numbers being called last. A value of 0
	 * represents a default priority. This value is assumed to be static, and may
	 * have unexpected results from changing this value.
	 *
	 * @return The priority level of this listener.
	 */
	public int getPriority();
}
