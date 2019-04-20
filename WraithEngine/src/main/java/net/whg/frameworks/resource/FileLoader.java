package net.whg.frameworks.resource;

public interface FileLoader
{
	/**
	 * Gets the file formats that this file loader supports.
	 *
	 * @return An array of names of supported file types.
	 */
	String[] getTargetFileTypes();

	/**
	 * Creates a new ResourceFuture instance to load the given resource file
	 * with.
	 *
	 * @param resourceManager
	 *     - The resource manager currently in charge of loading this file.
	 * @param resourceFile
	 *     - The resource to load.
	 * @return The newly created resource future instance which will be used to
	 *     load the file.
	 */
	ResourceFuture loadFile(ResourceManager resourceManager,
			ResourceFile resourceFile);

	/**
	 * This methods creates a new instance for writing resource data into.
	 *
	 * @return a new data type instance for the type of resource that this file
	 *     loader represents.
	 */
	ResourceData createDataInstace();
}
