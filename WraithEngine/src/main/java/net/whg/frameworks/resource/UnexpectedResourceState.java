package net.whg.frameworks.resource;

/**
 * An error that is thrown when a resource attempts to enter an unknown state
 * and is unsure how to continue.
 *
 * @author TheDudeFromCI
 */
public class UnexpectedResourceState extends RuntimeException
{
	private static final long serialVersionUID = -2608316810061900855L;

	public UnexpectedResourceState(String message)
	{
		super(message);
	}
}
