package net.whg.frameworks.resource;

public interface FileLoader
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
	String[] getTargetFileTypes();

	/**
	 * Creates a new ResourceFuture instance to load the given resource file with.
	 *
	 * @param resourceManager
	 *            - The resource manager currently in charge of loading this file.
	 * @param resourceFile
	 *            - The resource to load.
	 * @return The newly created resource future instance which will be used to load
	 *         the file.
	 */
	ResourceFuture loadFile(ResourceManager resourceManager, ResourceFile resourceFile);

	/**
	 * This methods creates a new instance for writing resource data into.
	 *
	 * @return a new data type instance for the type of resource that this file
	 *         loader represents.
	 */
	ResourceData createDataInstace();
}
