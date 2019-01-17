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
	 * Gets the file type (file extension) of the specified pathname. If no pathname is specified,
	 * null is returned.
	 *
	 * @param pathname - The pathname to check.
	 *
	 * @return A string representing the file extension of this parhname, or null if this parhname
	 * does not have a file extention, or points to a folder.
	 */
	public static String getFileExtention(String pathname)
	{
		if (pathname == null)
		{
			Log.warn("Cannot get extention of null path name!");
			return null;
		}

		int lastDot = pathname.lastIndexOf(".");

		if (lastDot == -1)
			return null;

		return pathname.substring(lastDot + 1);
	}
}
