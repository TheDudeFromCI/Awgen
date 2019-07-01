package net.whg.frameworks.scene;

import java.io.Serializable;
import org.joml.Matrix4f;

/**
 * Represents a local transformation matrix of an object in 3D space.
 *
 * @author TheDudeFromCI
 */
public interface ITransform extends Serializable
{
	/**
	 * Calculates the local matrix for this transform, and stores it in the
	 * output matrix parameter.
	 *
	 * @param out
	 *     - The matrix to store the output into.
	 */
	void getLocalMatrix(Matrix4f out);

	/**
	 * Calculates the full matrix for this transform, given the input parent
	 * transform matrix, and stores it in the output matrix parameter. A full
	 * transform is considered <br>
	 * <br>
	 * <code>out = parent * local</code><br>
	 * <br>
	 * for the given transforms.
	 *
	 * @param parent
	 *     - The parent transform to reference.
	 * @param out
	 *     - The matrix to store the output into.
	 */
	void getFullMatrix(Matrix4f parent, Matrix4f out);
}
