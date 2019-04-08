package net.whg.frameworks.resource;

import java.io.File;
import java.util.List;

/**
 * Represents a database for loading plugins and plugin resources. ResourceFiles
 * may be read from or written to.
 *
 * @author TheDudeFromCI
 */
public interface FileDatabase
{
	/**
	 * Gets a list of all jar files which should be loaded as a library. It is
	 * assumed these can each be read from.
	 *
	 * @return A list of all jar files to load as a library.
	 */
	List<File> getJarLibraries();

	/**
	 * Gets the file on disk that this resource file points to.
	 * 
	 * @param file
	 *            - The resource file pointer.
	 * @return The file that this resource file points to.
	 */
	File getFile(ResourceFile file);
}
