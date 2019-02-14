package net.whg.we.ui;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import net.whg.we.utils.Transform;

public class Transform2D implements Transform
{
	private Transform _parent;
	private Vector2f _position = new Vector2f(0f, 0f);
	private Vector2f _size = new Vector2f(1f, 1f);
	private float _rotation;
	private Matrix4f _localMatrix = new Matrix4f();
	private Matrix4f _fullMatrix = new Matrix4f();

	public Vector2f getPosition()
	{
		return _position;
	}

	public void setPosition(Vector2f position)
	{
		_position.set(position);
	}

	public void setPosition(float x, float y)
	{
		_position.set(x, y);
	}

	public Vector2f getSize()
	{
		return _size;
	}

	public void setSize(Vector2f size)
	{
		_size.set(size);
	}

	public void setSize(float x, float y)
	{
		_size.set(x, y);
	}

	public float getRotation()
	{
		return _rotation;
	}

	public void setRotation(float r)
	{
		_rotation = r;
	}

	@Override
	public Matrix4f getLocalMatrix()
	{
		_localMatrix.identity();
		_localMatrix.translate(_position.x, _position.y, 0f);
		_localMatrix.rotateZ(_rotation);
		_localMatrix.scale(_size.x, _size.y, 1f);

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
