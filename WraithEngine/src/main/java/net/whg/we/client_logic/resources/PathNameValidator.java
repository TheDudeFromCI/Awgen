package net.whg.we.client_logic.resources;

public interface PathNameValidator
{
	/**
	 * Checks if the requested pathname is considered valid pathname as specified by
	 * this validator specifications.
	 *
	 * @param pathName
	 *            - The pathname to validate.
	 * @return True if this path name is considered valid. False otherwise.
	 */
	public boolean isValidPathName(String pathName);
}
