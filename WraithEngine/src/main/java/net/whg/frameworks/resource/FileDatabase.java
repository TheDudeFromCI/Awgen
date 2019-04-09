package net.whg.frameworks.resource;

import java.io.File;

/**
 * Represents a database for loading resources. ResourceFiles may be read from
 * or written to.
 *
 * @author TheDudeFromCI
 */
public interface FileDatabase
{
	/**
	 * Gets the file on disk that this resource file points to.
	 *
	 * @param file
	 *            - The resource file pointer.
	 * @return The file that this resource file points to.
	 */
	File getFile(ResourceFile file);
}
