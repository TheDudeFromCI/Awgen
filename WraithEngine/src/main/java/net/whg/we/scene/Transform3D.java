package net.whg.we.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform3D implements Transform
{
	private Transform _parent;
	private Vector3f _position = new Vector3f();
	private Quaternionf _rotation = new Quaternionf();
	private Vector3f _size = new Vector3f(1f, 1f, 1f);
	private Matrix4f _localMatrix = new Matrix4f();
	private Matrix4f _fullMatrix = new Matrix4f();

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
	public Matrix4f getLocalMatrix()
	{
		_localMatrix.identity();
		_localMatrix.translate(_position);
		_localMatrix.rotate(_rotation);
		_localMatrix.scale(_size);

		return _localMatrix;
	}

	@Override
	public Matrix4f getFullMatrix()
	{
		if (_parent == null)
			_fullMatrix.identity();
		else
			_fullMatrix.set(_parent.getFullMatrix());
		_fullMatrix.mul(getLocalMatrix());
		return _fullMatrix;
	}

	@Override
	public Transform getParent()
	{
		return _parent;
	}

	@Override
	public void setParent(Transform transform)
	{
		// Validate parent
		{
			Transform p = transform;
			while (p != null)
			{
				if (p == this)
					throw new IllegalArgumentException("Circular heirarchy detected!");
				p = p.getParent();
			}
		}

		_parent = transform;
	}
}
