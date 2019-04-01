package net.whg.frameworks.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform3D implements Transform
{
	private Vector3f _position = new Vector3f();
	private Quaternionf _rotation = new Quaternionf();
	private Vector3f _size = new Vector3f(1f, 1f, 1f);

	private Matrix4f _matrixBuffer = new Matrix4f();

	public Vector3f getPosition()
	{
		return _position;
	}

	public void setPosition(Vector3f position)
	{
		_position.set(position);
	}

	public void setPosition(float x, float y, float z)
	{
		_position.set(x, y, z);
	}

	public Vector3f getSize()
	{
		return _size;
	}

	public void setSize(Vector3f size)
	{
		_size.set(size);
	}

	public void setSize(float size)
	{
		_size.set(size, size, size);
	}

	public void setSize(float x, float y, float z)
	{
		_size.set(x, y, z);
	}

	public Quaternionf getRotation()
	{
		return _rotation;
	}

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
