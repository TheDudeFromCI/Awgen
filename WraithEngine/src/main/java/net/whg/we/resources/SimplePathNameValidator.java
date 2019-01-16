package net.whg.we.resources;

import net.whg.we.utils.Log;

/**
 * Considers a pathname to follow the specification of simple folder names
 * seperated by a forward slash. Each file or folder is expected to follow the
 * pattern of using only letters, numbers, spaces, or underscores. Names cannot
 * be empty, or end with a space or underscore. If the pathname points to a
 * file, the file is expected to contain a single period to indicate the file
 * extention. The file name cannot start or end with a period. Pathnames also
 * should not start or end with a forward slash.
 *
 * @author TheDudeFromCI
 */
public class SimplePathNameValidator implements PathNameValidator
{
	@Override
	public boolean isValidPathName(String pathName)
	{
		Log.tracef("Testing path name '%s' for validity via SimplePathNameValidator.", pathName);

		String allowedLetters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		char lastLetter = '\n';

		for (int i = 0; i < pathName.length(); i++)
		{
			char c = pathName.charAt(i);

			if (allowedLetters.indexOf(c) != -1)
			{
				lastLetter = c;
				continue;
			}

			if (c == '/')
			{
				if (allowedLetters.indexOf(lastLetter) == -1)
				{
					Log.tracef("  Failed to validate pathname. Incorrectly placed '/'");
					return false;
				}

				lastLetter = c;
				continue;
			}

			if (c == '_' || c == ' ')
			{
				if (allowedLetters.indexOf(lastLetter) == -1)
				{
					Log.tracef("  Failed to validate pathname. Incorrectly placed '_' or ' '");
					return false;
				}

				lastLetter = c;
				continue;
			}

			if (c == '.')
			{
				if (allowedLetters.indexOf(lastLetter) == -1)
				{
					Log.tracef("  Failed to validate pathname. Incorrectly placed '.'");
					return false;
				}

				lastLetter = c;
				continue;
			}

			Log.tracef("  Failed to validate pathname. Unknown character.");
			return false;
		}

		if (allowedLetters.indexOf(lastLetter) == -1)
		{
			Log.tracef("  Failed to validate pathname. Pathname ends with incorrect character.");
			return false;
		}

		return true;
	}
}
