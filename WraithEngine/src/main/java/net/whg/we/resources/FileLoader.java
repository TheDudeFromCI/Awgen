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
	 * Loads a specfic file as a resource. This method may be called from any
	 * thread.
	 *
	 * @param file
	 *            - The file to load.
	 * @param properties
	 *            - The properties file attached to this file, or null if no
	 *            properties file was found.
	 * @return A resource representing the file, or null if the file could not be
	 *         loaded.
	 */
	public FileLoadState loadFile(ResourceBatchRequest request, ResourceFile resource);

	/**
	 * Gets the priority level for this file loader. When loading a file, the file
	 * loader with the highest priority value is used. The default priority value is
	 * 0.
	 *
	 * @return The priority value of this file loader.
	 */
	public int getPriority();
}
