package net.whg.we.resources;

public interface FileLoader<T>
{
	/**
	 * Gets a string array of file types that are supported by the this file loader.
	 * File types are a string representing the extention type of a file.<br>
	 * <br>
	 * Examples:
	 *
	 * <pre>
	 *   "png", "txt", "jpeg", "html"
	 * </pre>
	 *
	 * @return A String array of supported file types.
	 */
	public String[] getTargetFileTypes();

	/**
	 * Loads a specfic file as a resource. The resource is added to the database
	 * after it is loaded. All resources in the give file are loaded to the
	 * database, but only the file with the given name is returned.
	 *
	 * @param resourceLoader
	 *            - The resource loader currently in charge of loading this file.
	 * @param database
	 *            - The resource database currently being used.
	 * @param resourceFile
	 *            - The resource to load.
	 */
	public Resource<T> loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile);

	/**
	 * Gets the priority level for this file loader. When loading a file, the file
	 * loader with the highest priority value is used. The default priority value is
	 * 0.
	 *
	 * @return The priority value of this file loader.
	 */
	public int getPriority();
}
