package net.whg.we.scene;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import net.whg.frameworks.scene.Transform;

public class Transform2D implements Transform
{
	private Transform2D _parent;
	private Vector2f _position = new Vector2f(0f, 0f);
	private Vector2f _size = new Vector2f(1f, 1f);
	private float _rotation;

	private Matrix4f _matrixBuffer = new Matrix4f();
	private Matrix4f _matrixBuffer2 = new Matrix4f();

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
	public void getLocalMatrix(Matrix4f out)
	{
		out.identity();
		out.translate(_position.x, _position.y, 0f);
		out.rotateZ(_rotation);
		out.scale(_size.x, _size.y, 1f);
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

	public Matrix4f getFullMatrixFast()
	{
		if (_parent == null)
		{
			getLocalMatrix(_matrixBuffer);
			return _matrixBuffer;
		}

		_matrixBuffer.set(_parent.getFullMatrixFast());
		getLocalMatrix(_matrixBuffer2);
		_matrixBuffer.mul(_matrixBuffer2);

		return _matrixBuffer;
	}

	public Transform2D getParent()
	{
		return _parent;
	}

	public void setParent(Transform2D parent)
	{
		_parent = parent;
	}
}
