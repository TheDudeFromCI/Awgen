package net.whg.frameworks.resource;

import net.whg.frameworks.util.FileUtils;

/**
 * A resource file is a pointer to a loaded or unloaded resource.
 *
 * @author TheDudeFromCI
 */
public class ResourceFile
{
	private String _pathname;
	private String _extension;

	/**
	 * Creates a pointer to a resource from the given pathname. The pathname is
	 * broken down into two parts, the pathname of the file for the resource, and
	 * the name of the resource within the file. An example:<br>
	 * <br>
	 * <code>path/to/file.fbx:base_human</code><br>
	 * <br>
	 * If the resource name is not specified, it is assigned as "default", meaning
	 * the first asset within the file.
	 */
	public ResourceFile(String pathname)
	{
		if (!FileUtils.isValidPathname(pathname))
			throw new IllegalArgumentException("Pathname not valid! '" + pathname + "'");

		_pathname = pathname;
		_extension = FileUtils.getFileExtention(pathname);
	}

	/**
	 * Gets the pathname of this resource file.
	 *
	 * @return The name of this resource file and it's relative path.
	 */
	public String getPathname()
	{
		return _pathname;
	}

	/**
	 * Gets the file extension for this resource.
	 *
	 * @return The file extension for this resource.
	 */
	public String getFileExtension()
	{
		return _extension;
	}

	@Override
	public String toString()
	{
		return "[Res: " + _pathname + "]";
	}

	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof ResourceFile))
			return false;

		ResourceFile a = (ResourceFile) other;
		return _pathname.equals(a._pathname);
	}

	@Override
	public int hashCode()
	{
		return _pathname.hashCode();
	}
}
