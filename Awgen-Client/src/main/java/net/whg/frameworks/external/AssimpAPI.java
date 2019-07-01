package net.whg.frameworks.external;

import java.io.File;

/**
 * This class acts as a bridge to the Assimp api, to allow for meshes to be
 * loaded in a predictable manner.
 *
 * @author TheDudeFromCI
 */
public interface AssimpAPI
{
	/**
	 * Loads a file as a scene.
	 * 
	 * @param file
	 *     - The mesh file to load.
	 * @return A scene instance version of this file, containing all meshes and
	 *     hierarchies, or null if the scene could not be loaded.
	 */
	AssimpScene load(File file);
}
