package net.whg.we.client_logic.resources;

import net.whg.we.main.Plugin;
import net.whg.we.client_logic.rendering.Graphics;

/**
 * This class handles all high level API for resource management.
 *
 * @author TheDudeFromCI
 */
public class ResourceManager
{
    private ResourceDatabase _resourceDatabase;
    private ResourceLoader _resourceLoader;
    private FileDatabase _fileDatabase;

    /**
     * Creates a new resource manager to manage the injected resource handlers.
     *
     * @param resourceDatabase
     *                             - The database which holds all currently
     *                             loaded resources.
     * @param resourceLoader
     *                             - The loader which handles loading new
     *                             resources.
     * @param fileDatabase
     *                             - The database which handles file processing.
     */
    public ResourceManager(ResourceDatabase resourceDatabase,
            ResourceLoader resourceLoader, FileDatabase fileDatabase)
    {
        _resourceDatabase = resourceDatabase;
        _resourceLoader = resourceLoader;
        _fileDatabase = fileDatabase;
    }

    /**
     * Gets the resource database this manager currently controls.
     *
     * @return The resource database.
     */
    public ResourceDatabase getResourceDatabase()
    {
        return _resourceDatabase;
    }

    /**
     * Gets the resource loader this manager currently controls.
     *
     * @return The resource loader.
     */
    public ResourceLoader getResourceLoader()
    {
        return _resourceLoader;
    }

    /**
     * Gets the file database this manager currently controls.
     *
     * @return The file database.
     */
    public FileDatabase getFileDatabase()
    {
        return _fileDatabase;
    }

    /**
     * Loads a resource from a resource file and returns the result. If the file
     * is already loaded, then the loaded isntance of the resource is returned
     * instead.
     *
     * @param resourceFile
     *                         - The resource file to load.
     * @return The loaded resource, or null if the resource could not be loaded.
     */
    public Resource loadResource(ResourceFile resourceFile)
    {
        return _resourceLoader.loadResource(resourceFile, _resourceDatabase);
    }

    /**
     * Creates a resource file instance for the given plugin and resource
     * pathname.
     *
     * @param plugin
     *                     - The plugin which owns this resource.
     * @param pathname
     *                     - The pathname of the resource.
     * @return A resource file instance for the give plugin and pathname.
     */
    public ResourceFile getResourceFile(Plugin plugin, String pathname)
    {
        return _fileDatabase.getResourceFile(plugin, pathname);
    }

    /**
     * Loads a resource from a plugin and pathname. This method is a shorthand
     * for calling<br>
     * <code>loadResource(getResourceFile(plugin, pathname));</code><br>
     *
     * @param plugin
     *                     - The plugin that owns the resource to load.
     * @param pathname
     *                     - The pathname of the resource to load.
     * @return The resource at the given pathname, owned by the give plugin, or
     *         null if the resource could not be loaded.
     */
    public Resource loadResource(Plugin plugin, String pathname)
    {
        return loadResource(getResourceFile(plugin, pathname));
    }

    /**
     * Disposes all resources currently loading within the database.
     */
    public void disposeAllResources()
    {
        _resourceDatabase.dispose();
    }

    /**
     * Compiles all currently loaded resources if they are not already compiled.
     *
     * @param graphics
     *                     - The graphics manager to compile resources with.
     */
    public void compileAllResources(Graphics graphics)
    {
        _resourceDatabase.compileAllResources(graphics);
    }
}
