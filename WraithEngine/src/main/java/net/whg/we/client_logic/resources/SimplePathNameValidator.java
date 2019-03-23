package net.whg.we.client_logic.resources;

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
public class SimplePathNameValidator implements PathNameValidator
{
	@Override
	public boolean isValidPathName(String pathName)
	{
		
//		The different parts of the regex:
//		^[a-zA-Z0-9] the path should start with a letter or number (not a space or anything else)
//		([a-zA-Z0-9 _]+\/)* then the path can contain any number of letters/numbers/'_'/' ' followed by a '/'
//		following this is an OR statement: (([a-zA-Z0-9 _]+\.[a-zA-Z0-9 _]*[a-zA-Z0-9](:(?!.*:.*).*)?)|[a-zA-Z0-9 _]*[a-zA-Z0-9])$
//		it can be divided up into 2 statements; statement 1 OR statement 2
//		statement 1: ([a-zA-Z0-9 _]+\.[a-zA-Z0-9 _]*[a-zA-Z0-9](:(?!.*:.*).*)?)
//		matches letters/numbers/'_'/' ' followed by a '.' followed by more letters/numbers/'_'/' ' and ends in a letter/number.
//		(:(?!.*:.*).*)?) is a negative lookahead, and it makes sure that there can be a ':' followed by anything except another ':'
//		statement 2: [a-zA-Z0-9 _]*[a-zA-Z0-9]
//		matches a normal pattern of any number of letters/numbers/'_'/' ' ending with a letter/number
		
		String regex = "^[a-zA-Z0-9]([a-zA-Z0-9 _]+\\/)*(([a-zA-Z0-9 _]+\\.[a-zA-Z0-9 _]*[a-zA-Z0-9](:(?!.*:.*).*)?)|[a-zA-Z0-9 _]*[a-zA-Z0-9])$";

		return pathName.matches(regex);
		
	}
}
