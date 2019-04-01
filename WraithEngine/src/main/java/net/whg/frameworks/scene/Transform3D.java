package net.whg.frameworks.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Represents a standard, 3D variation of the transform interface.
 *
 * @author TheDudeFromCI
 */
public class Transform3D implements ITransform
{
	// Fields
	private Vector3f _position = new Vector3f();
	private Quaternionf _rotation = new Quaternionf();
	private Vector3f _size = new Vector3f(1f, 1f, 1f);

	// Temp
	private Matrix4f _matrixBuffer = new Matrix4f();

	/**
	 * Gets the position of this transform in 3D space.
	 *
	 * @return The position vector for this transform.
	 */
	public Vector3f getPosition()
	{
		return _position;
	}

	/**
	 * Sets the position of this transform based on the values given by the input.
	 *
	 * @param position
	 *            - The position vector to copy the values from.
	 */
	public void setPosition(Vector3f position)
	{
		_position.set(position);
	}

	/**
	 * Sets the position of this transform based on the values given by the input.
	 *
	 * @param x
	 *            - The x position.
	 * @param y
	 *            - The y position.
	 * @param z
	 *            - The z position.
	 */
	public void setPosition(float x, float y, float z)
	{
		_position.set(x, y, z);
	}

	/**
	 * Gets the size/scaling vector for this transform.
	 *
	 * @return The size vector for this transform.
	 */
	public Vector3f getSize()
	{
		return _size;
	}

	/**
	 * Gets the size of this transform based on the values given by the inout.
	 *
	 * @param size
	 *            - The size vector to copt the values from.
	 */
	public void setSize(Vector3f size)
	{
		_size.set(size);
	}

	/**
	 * Gets the size of this transform based on the values given by the inout.
	 *
	 * @param size
	 *            - The uniform scaling value.
	 */
	public void setSize(float size)
	{
		_size.set(size, size, size);
	}

	/**
	 * Gets the size of this transform based on the values given by the inout.
	 *
	 * @param x
	 *            - The x scale.
	 * @param y
	 *            - The y scale.
	 * @param z
	 *            - The z scale.
	 */
	public void setSize(float x, float y, float z)
	{
		_size.set(x, y, z);
	}

	/**
	 * Gets the current rotation of this transform.
	 * 
	 * @return The rotation of this transform.
	 */
	public Quaternionf getRotation()
	{
		return _rotation;
	}

	/**
	 * Sets the rotation of this transform.
	 * 
	 * @param rot
	 *            - The quaternion to copy the rotation from.
	 */
	public void setRotation(Quaternionf rot)
	{
		_rotation.set(rot);
	}

	@Override
	public void getLocalMatrix(Matrix4f out)
	{
		out.identity();
		out.translate(_position);
		out.rotate(_rotation);
		out.scale(_size);
	}

	@Override
	public void getFullMatrix(Matrix4f parent, Matrix4f out)
	{
		if (parent == null)
		{
			getLocalMatrix(out);
			return;
		}

		out.set(parent);
		getLocalMatrix(_matrixBuffer);
		out.mul(_matrixBuffer);
	}
}
