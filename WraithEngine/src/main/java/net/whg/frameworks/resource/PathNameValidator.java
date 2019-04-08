package net.whg.frameworks.resource;

/**
 * Considers a pathname to follow the specification of simple folder names
 * separated by a forward slash. Each file or folder is expected to follow the
 * pattern of using only letters, numbers, spaces, or underscores. Names cannot
 * be empty, or end with a space or underscore. If the pathname points to a
 * file, the file is expected to contain a single period to indicate the file
 * extension. The file name cannot start or end with a period. Pathnames also
 * should not start or end with a forward slash.<br>
 * <br>
 * If a file with an extension is specified, then a colon can be used at the end
 * to indicate a resource within that file. If this is not specified, the file
 * name itself is used instead. For a resource name, any character can be used
 * with the exception of colons.<br>
 * <br>
 * <code>
 * Examples:<br>
 * * file.png<br>
 * * path/to/file.txt<br>
 * * my/file.fbx:mesh_27<br>
 * </code>
 *
 * @author TheDudeFromCI
 * @author luminiusa
 */
public class PathNameValidator
{
	public boolean isValidPathName(String pathName)
	{
		String regex =
				"^[a-zA-Z0-9]([a-zA-Z0-9 _]+\\/)*(([a-zA-Z0-9 _]+\\.[a-zA-Z0-9 _]*[a-zA-Z0-9](:(?!.*:.*).*)?)|[a-zA-Z0-9 _]*[a-zA-Z0-9])$";
		return pathName.matches(regex);
	}
}
