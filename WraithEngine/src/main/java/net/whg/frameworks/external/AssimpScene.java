package net.whg.frameworks.external;

/**
 * Represents a loaded scene within Assimp.
 *
 * @author TheDudeFromCI
 */
public interface AssimpScene
{
	/**
	 * Gets the numbers of meshes within this scene.
	 *
	 * @return The number of meshes.
	 */
	int getMeshCount();

	/**
	 * Gets the mesh at the specified index.
	 *
	 * @param index
	 *     - The index of the mesh.
	 * @return The mesh.
	 */
	AssimpMesh getMesh(int index);
}
