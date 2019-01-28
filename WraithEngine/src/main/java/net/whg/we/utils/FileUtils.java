package net.whg.we.utils;

/**
 * A collection of various utility functions for dealing with file and folder
 * management.
 *
 * @author TheDudeFromCI
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
	 * @return The regular name of the folder or file.
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
}
