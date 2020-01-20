package net.whg.awgen.lib.util;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a set of useful utilities for reading build information
 * about Awgen.
 */
public final class Version
{
    private static final Logger logger = LoggerFactory.getLogger(Version.class);

    /**
     * Loads the current build information into a version object.
     * 
     * @return The loaded version object, or null if the build information could not
     *     be loaded.
     */
    public static Version get()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(Version.class.getClassLoader()
                                         .getResourceAsStream("project.properties"));

            return new Version(properties);
        }
        catch (IOException exception)
        {
            logger.error("Failed to read version information!", exception);
            return null;
        }
    }

    private final Properties properties;

    /**
     * Creates a new version object with the given properties.
     * 
     * @param properties
     *     - The properties for the build.
     */
    private Version(Properties properties)
    {
        this.properties = properties;
    }

    /**
     * Gets the build number. Values are returned in the format of
     * <code>build_#</code> where "#" the build index, starting at 1. For
     * development builds, the string "dev_build" is returned instead.
     * 
     * @return The build number, or "unassigned" if the build information was not
     *     assigned.
     */
    public String getBuildNumber()
    {
        return properties.getProperty("build", "unassigned");
    }

    /**
     * Prints the standard version loading information to the console.
     */
    public void printInfo()
    {
        logger.info("Loading Awgen:");
        logger.info("  Build: {}", getBuildNumber());
    }
}
