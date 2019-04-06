package net.whg.frameworks.resource;

import java.io.File;
import java.util.List;
import net.whg.we.main.Plugin;

/**
 * Represents a database for loading plugins and plugin resources. ResourceFiles may be read from or
 * written to.
 *
 * @author TheDudeFromCI
 */
public interface FileDatabase
{
	/**
	 * Gets a list of all jar files which should be loaded as a library. It is assumed these can
	 * each be read from.
	 *
	 * @return A list of all jar files to load as a library.
	 */
	public List<File> getJarLibraries();

	/**
	 * Gets a resource file for the requested plugin and the path name of that resource file. If the
	 * resource file does not exist, then an empty resource file is returned for purposes of writing
	 * to. If a plugin is not defined, this method returns null.
	 *
	 * @param plugin - The plugin what owns this resource.
	 * @param pathName - The path name of this resource file. The pathname should be validated as
	 * defined by this database's @{link #getPathNameValidator()}
	 *
	 * @return The ResourceFile at the requested destination, or null if the plugin is not defined,
	 * or the path cannot be parsed.
	 */
	public ResourceFile getResourceFile(Plugin plugin, String pathName);

	/**
	 * Gets the class in charge of validating pathnames for this FileDatabase. Different
	 * implementations my require pathnames to be given in a different method.
	 *
	 * @return The PathNameValidator being used for this database.
	 */
	public PathNameValidator getPathNameValidator();
}
