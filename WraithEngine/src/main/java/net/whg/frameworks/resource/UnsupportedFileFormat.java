package net.whg.frameworks.resource;

/**
 * Represents an error with attempting to handle a file who's file extention or
 * file format is unknown.
 * 
 * @author TheDudeFromCI
 */
public class UnsupportedFileFormat extends RuntimeException
{
	private static final long serialVersionUID = 6422459155062608823L;

	public UnsupportedFileFormat(String message)
	{
		super(message);
	}
}
