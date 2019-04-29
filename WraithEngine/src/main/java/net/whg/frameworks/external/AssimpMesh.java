package net.whg.frameworks.external;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 * A representation of a mesh object that exists within Assimp.
 *
 * @author TheDudeFromCI
 */
public interface AssimpMesh
{
	/**
	 * Gets the name of this mesh.
	 *
	 * @return The name of this mesh.
	 */
	String getName();

	/**
	 * Gets the number of bones within this mesh, or that this mesh depends on.
	 *
	 * @return The number of bones.
	 */
	int getBoneCount();

	/**
	 * Gets the number of vertices within this mesh.
	 *
	 * @return The number of vertices.
	 */
	int getVertexCount();

	/**
	 * Gets the number of triangles within this mesh.
	 *
	 * @return The number of triangles.
	 */
	int getTriangleCount();

	/**
	 * Checks if this mesh has any tangents generated for it. This will usually
	 * return false if this mesh has no texture coords or if the tangents could not
	 * be loaded or generated by Assimp.
	 *
	 * @return True if this mesh has tangents, false otherwise.
	 */
	boolean hasTangents();

	/**
	 * Checks if this mesh has any bitangents generated for it. This will usually
	 * return false if this mesh has no texture coords or if the bitangents could
	 * not be loaded or generated by Assimp.
	 *
	 * @return True if this mesh has bitangents, false otherwise.
	 */
	boolean hasBitangents();

	/**
	 * Gets the number of UV sets that are attached to this mesh. If this returns 0,
	 * then this mesh has no texture coords. Usually meshes will only have one. In
	 * the case of multiple sets of texture coords, the first UV is the default.
	 *
	 * @return THe number of UV sets attached to this mesh.
	 */
	int getUVCount();

	/**
	 * Gets the position of a vertex.
	 * 
	 * @param index
	 *     - The index of a vertex.
	 * @return The position.
	 */
	Vector3f getVertPosition(int index);

	/**
	 * Gets the normal of a vertex.
	 * 
	 * @param index
	 *     - The normal of a vertex.
	 * @return The normal.
	 */
	Vector3f getVertNormal(int index);

	/**
	 * Gets the texture coord of a vertex using a specified UV set.
	 * 
	 * @param index
	 *     - The index of a vertex.
	 * @param uvLayer
	 *     - The UV set to use. 0 is default.
	 * @return The texture coords.
	 */
	Vector2f getVertUV(int index, int uvLayer);

	/**
	 * Gets the tangent of a vertex.
	 * 
	 * @param index
	 *     - The index of a vertex.
	 * @return The tangent.
	 */
	Vector3f getVertTangent(int index);

	/**
	 * Gets the bitangent of a vertex.
	 * 
	 * @param index
	 *     - The index of a vertex.
	 * @return The bitangent.
	 */
	Vector3f getVertBitangent(int index);

	/**
	 * Gets a triangle based on the three vertices that it points to. The values
	 * returned are the indices of those vertices.
	 * 
	 * @param index
	 *     - The index of a triangle.
	 * @return The triangle.
	 */
	Vector3i getTriangle(int index);
}
