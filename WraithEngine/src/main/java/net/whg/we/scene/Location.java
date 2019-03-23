package net.whg.we.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Location
{
	private Vector3f _position;
	private Quaternionf _rotation;
	private Vector3f _scale;
	private Matrix4f _transformMatrix;
	private Matrix4f _inverseTransformMatrix;

	private Vector3f _vectorBuffer = new Vector3f();
	private Quaternionf _quaternionBuffer = new Quaternionf();

	public Location(Vector3f pos, Quaternionf rot, Vector3f scale)
	{
		_position = new Vector3f(pos);
		_rotation = new Quaternionf(rot);
		_scale = new Vector3f(scale);
		_transformMatrix = new Matrix4f();
		_inverseTransformMatrix = new Matrix4f();
	}

	public Location(Vector3f pos, Quaternionf rot)
	{
		this(pos, rot, new Vector3f(1f, 1f, 1f));
	}

	public Location(Vector3f pos)
	{
		this(pos, new Quaternionf(), new Vector3f(1f, 1f, 1f));
	}

	public Location()
	{
		this(new Vector3f(), new Quaternionf(), new Vector3f(1f, 1f, 1f));
	}

	public Location(Location location)
	{
		this(location._position, location._rotation, location._scale);
	}

	private void updateMatrix()
	{
		_transformMatrix.identity();
		_transformMatrix.translate(_position);
		_transformMatrix.rotate(_rotation);
		_transformMatrix.scale(_scale);

		_inverseTransformMatrix.identity();
		_inverseTransformMatrix.rotate(_rotation.invert(_quaternionBuffer));
		_inverseTransformMatrix.translate(_position.negate(_vectorBuffer));
		_inverseTransformMatrix.scale(_scale.negate(_vectorBuffer));
	}

	public Vector3f getPosition()
	{
		return new Vector3f(_position);
	}

	public Quaternionf getRotation()
	{
		return new Quaternionf(_rotation);
	}

	public Vector3f getScale()
	{
		return new Vector3f(_scale);
	}

	public void setPosition(Vector3f pos)
	{
		_position.set(pos);
		updateMatrix();
	}

	public void setRotation(Quaternionf rot)
	{
		_rotation.set(rot);
		updateMatrix();
	}

	public void setScale(Vector3f scale)
	{
		_scale.set(scale);
		updateMatrix();
	}

	public Vector3f getPosition(Vector3f buffer)
	{
		buffer.set(_position);
		return buffer;
	}

	public Quaternionf getRotation(Quaternionf buffer)
	{
		buffer.set(_rotation);
		return buffer;
	}

	public Vector3f getScale(Vector3f buffer)
	{
		buffer.set(_scale);
		return buffer;
	}

	public Matrix4f getMatrix()
	{
		return new Matrix4f(_transformMatrix);
	}

	public Matrix4f getMatrix(Matrix4f buffer)
	{
		buffer.set(_transformMatrix);
		return buffer;
	}

	public Matrix4f getInverseMatrix()
	{
		return new Matrix4f(_inverseTransformMatrix);
	}

	public Matrix4f getInverseMatrix(Matrix4f buffer)
	{
		buffer.set(_inverseTransformMatrix);
		return buffer;
	}
}
