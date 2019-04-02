package net.whg.frameworks.util;

import java.io.File;

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
		int colon = pathname.lastIndexOf(":");

		if (lastDot == -1)
			return null;

		if (colon == -1 || colon < lastDot)
			return pathname.substring(lastDot + 1);
		return pathname.substring(lastDot + 1, colon);
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
		int colon = pathname.lastIndexOf(":");

		if (lastSlash == -1)
		{
			if (colon == -1)
				return pathname;
			return pathname.substring(0, colon);
		}

		if (colon == -1 || colon < lastSlash)
			return pathname.substring(lastSlash + 1);
		return pathname.substring(lastSlash + 1, colon);
	}

	/**
	 * Gets the file path of this pathname without the resource pointer. If no
	 * resource pointer is given, the original pathname is returned.
	 *
	 * @param pathname
	 *            - A pathname
	 * @return The pathname without the resource pointer, or null if pathname is
	 *         null.
	 */
	public static String getPathnameWithoutResource(String pathname)
	{
		if (pathname == null)
			return null;

		int colon = pathname.lastIndexOf(":");

		if (colon == -1)
			return pathname;

		return pathname.substring(0, colon);
	}

	/**
	 * Gets the resource pointer for this pathname.
	 *
	 * @param pathname
	 *            - A pathname
	 * @return The pathname resource pointer, or null if pathname is null.
	 */
	public static String getPathnameOnlyResource(String pathname)
	{
		if (pathname == null)
			return null;

		int colon = pathname.lastIndexOf(":");

		if (colon == -1)
			return getSimpleFileName(pathname);

		return pathname.substring(colon + 1);
	}

	public static String pathnameToFile(String pathname)
	{
		return getPathnameWithoutResource(pathname).replace('/', File.separatorChar);
	}
}
