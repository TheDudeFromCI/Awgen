package net.whg.we.resources;

/**
 * Considers a pathname to follow the specification of simple folder names
 * seperated by a forward slash. Each file or folder is expected to follow the
 * pattern of using only letters, numbers, spaces, or underscores. Names cannot
 * be empty, or end with a space or underscore. If the pathname points to a
 * file, the file is expected to contain a single period to indicate the file
 * extention. The file name cannot start or end with a period. Pathnames also
 * should not start or end with a forward slash.<br>
 * <br>
 * If a file with an extention is specified, then a colon can be used at the end
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
 */
public class SimplePathNameValidator implements PathNameValidator
{
	@Override
	public boolean isValidPathName(String pathName)
	{
		String allowedLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		char lastLetter = '\n';
		boolean nameMode = false;
		boolean hasFileExtention = false;

		for (int i = 0; i < pathName.length(); i++)
		{
			char c = pathName.charAt(i);

			if (nameMode)
			{
				if (c == ':')
					return false;

				lastLetter = c;
				continue;
			}
			else
			{
				if (allowedLetters.indexOf(c) != -1)
				{
					lastLetter = c;
					continue;
				}

				if (c == '/' || c == '_' || c == ' ' || c == '.')
				{
					if (allowedLetters.indexOf(lastLetter) == -1)
						return false;

					if (c == '.')
					{
						if (hasFileExtention)
							return false;

						hasFileExtention = true;
					}
					lastLetter = c;
					continue;
				}

				if (c == ':')
				{
					if (allowedLetters.indexOf(lastLetter) == -1)
						return false;

					if (!hasFileExtention)
						return false;

					nameMode = true;
					lastLetter = c;
					continue;
				}
			}

			return false;
		}

		if (!nameMode && allowedLetters.indexOf(lastLetter) == -1)
			return false;

		return true;
	}
}
