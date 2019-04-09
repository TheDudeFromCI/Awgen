package net.whg.frameworks.util;

/**
 * A collection of various utility functions for dealing with file and folder
 * management.
 *
 * @author TheDudeFromCI
 * @author luminiusa
 */
public class FileUtils
{
	/**
	 * Gets the file type (file extension) of the specified pathname. If no pathname
	 * is specified, null is returned.
	 *
	 * @param pathname
	 *            - The pathname to check.
	 * @return A string representing the file extension of this parhname, or null if
	 *         this parhname does not have a file extention, or points to a folder.
	 */
	public static String getFileExtention(String pathname)
	{
		if (pathname == null)
			return null;

		int lastDot = pathname.lastIndexOf(".");

		if (lastDot == -1)
			return null;

		return pathname.substring(lastDot + 1);
	}

	/**
	 * Gets the simplified file name for this path name. This returns the name of
	 * the file, without the partent folder heirarchy.
	 *
	 * @param pathname
	 *            - A pathname of a folder or file.
	 * @return The regular name of the folder or file, or null if pathname is null.
	 */
	public static String getSimpleFileName(String pathname)
	{
		if (pathname == null)
			return null;

		int lastSlash = pathname.lastIndexOf("/");

		if (lastSlash == -1)
			return pathname;

		return pathname.substring(lastSlash + 1);
	}

	/**
	 * Considers a pathname to follow the specification of simple folder names
	 * separated by a forward slash. Each file or folder is expected to follow the
	 * pattern of using only letters, numbers, spaces, or underscores. Names cannot
	 * be empty, or end with a space. The file the path points to is expected to
	 * contain a single period to indicate the file extension. The file name cannot
	 * start or end with a period. Pathnames also should not start or end with a
	 * forward slash.
	 *
	 * @param pathname
	 *            - The pathname to validate.
	 * @return True if the pathname is a valid pathname, false otherwise.
	 */
	public static boolean isValidPathname(String pathname)
	{
		return pathname
				.matches("^(([\\w][\\w ]*[\\w]|[\\w])\\/?)*([\\w][\\w ]*[\\w]|[\\w])\\.\\w+$");
	}
}
