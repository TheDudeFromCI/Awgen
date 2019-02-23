package net.whg.we.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.whg.we.rendering.Camera;

public abstract class PersonCamera {

	protected static final float MAX_ANGLE = (float) Math.toRadians(89);
	protected static final float TAU = (float) Math.PI * 2f;

	protected Camera _camera;
	protected Vector3f _baseRotation;
	protected Vector3f _extraRotation;
	private float _mouseSensitivity = 3f;
	private float _moveSpeed = 7f;

	protected Vector3f _rotationBuffer = new Vector3f();
	protected Quaternionf _rotationStorageBuffer = new Quaternionf();

	public void setMoveSpeed(float moveSpeed)
	{
		_moveSpeed = moveSpeed;
	}

	public float getMoveSpeed()
	{
		return _moveSpeed;
	}

	public void setMouseSensitivity(float mouseSensitivity)
	{
		_mouseSensitivity = mouseSensitivity;
	}

	public float getMouseSensitivity()
	{
		return _mouseSensitivity;
	}

	public Vector3f getBaseRotation(Vector3f buffer)
	{
		buffer.set(_baseRotation);
		return buffer;
	}

	public Vector3f getBaseRotation()
	{
		return getBaseRotation(new Vector3f());
	}

	public Vector3f getExtraRotation(Vector3f buffer)
	{
		buffer.set(_extraRotation);
		return buffer;
	}

	public Vector3f getExtraRotation()
	{
		return getExtraRotation(new Vector3f());
	}

	public abstract void updateCameraRotation();

	public abstract void updateCameraPosition();

	public Location getLocation()
	{
		return _camera.getLocation();
	}

	public void update()
	{
		updateCameraRotation();
		updateCameraPosition();
	}
}

