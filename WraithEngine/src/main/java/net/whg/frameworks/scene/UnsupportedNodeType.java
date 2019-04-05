package net.whg.frameworks.scene;

/**
 * An exception that is thrown when a scene node of an unknown type is attempted
 * to be loaded.
 *
 * @author TheDudeFromCI
 */
public class UnsupportedNodeType extends RuntimeException
{
	private static final long serialVersionUID = 7269034357921543482L;

	public UnsupportedNodeType(String message)
	{
		super(message);
	}
}
