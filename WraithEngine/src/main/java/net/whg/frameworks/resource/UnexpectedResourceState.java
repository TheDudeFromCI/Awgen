package net.whg.frameworks.resource;

public class UnexpectedResourceState extends RuntimeException
{
	private static final long serialVersionUID = -2608316810061900855L;

	public UnexpectedResourceState(String message)
	{
		super(message);
	}
}
