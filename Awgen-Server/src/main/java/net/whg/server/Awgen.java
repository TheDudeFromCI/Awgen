package net.whg.server;

import net.whg.awgen.lib.util.Version;

/**
 * The entry point for the Awgen server.
 */
public final class Awgen
{
    /**
     * Starts the Awgen command-line server.
     * 
     * @param args
     *     - The arguments for configuring the launch properties.
     */
    public static void main(String[] args)
    {
        Version.get()
               .printInfo();
    }
}
