package net.whg.we.resource;

import java.io.File;
import java.util.List;
import net.whg.frameworks.resource.FileDatabase;

public interface ServerFileDatabase extends FileDatabase
{
	/**
	 * Gets a list of all jar files which should be loaded as a library. It is
	 * assumed these can each be read from.
	 *
	 * @return A list of all jar files to load as a library.
	 */
	List<File> getJarLibraries();
}
