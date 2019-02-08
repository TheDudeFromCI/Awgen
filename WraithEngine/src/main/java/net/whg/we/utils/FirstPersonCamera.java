package net.whg.we.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.rendering.Camera;

public class FirstPersonCamera
{
	private static final float MAX_ANGLE = (float) Math.toRadians(89);
	private static final float TAU = (float) Math.PI * 2f;

	private Camera _camera;
	private Vector3f _baseRotation;
	private Vector3f _extraRotation;
	private float _mouseSensitivity = 3f;
	private float _moveSpeed = 7f;

	private Vector3f _rotationBuffer = new Vector3f();
	private Quaternionf _rotationStorageBuffer = new Quaternionf();

	public FirstPersonCamera(Camera camera)
	{
		_camera = camera;
		_baseRotation = new Vector3f();
		_extraRotation = new Vector3f();
	}

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

	private void updateCameraRotation()
	{
		if (!Screen.isMouseLocked())
			return;

		float yaw = Input.getDeltaMouseX() * Time.deltaTime() * _mouseSensitivity;
		float pitch = Input.getDeltaMouseY() * Time.deltaTime() * _mouseSensitivity;

		_rotationBuffer.x = clamp(_baseRotation.x - pitch, -MAX_ANGLE, MAX_ANGLE);
		_rotationBuffer.y = (_baseRotation.y - yaw) % TAU;
		_rotationBuffer.z = _baseRotation.z;

		if (!isValid(_rotationBuffer))
			return;

		_baseRotation.set(_rotationBuffer);
		_rotationBuffer.add(_extraRotation);

		_rotationStorageBuffer.identity();
		_rotationStorageBuffer.rotateY(_rotationBuffer.y);
		_rotationStorageBuffer.rotateX(_rotationBuffer.x);
		_rotationStorageBuffer.rotateZ(_rotationBuffer.z);
		_camera.getLocation().setRotation(_rotationStorageBuffer);
	}

	private void updateCameraPosition()
	{
		if (!Screen.isMouseLocked())
			return;

		float move = Time.deltaTime() * _moveSpeed;
		Vector3f pos = _camera.getLocation().getPosition();
		Vector3f backward = _camera.getLocation().getInverseMatrix().positiveZ(new Vector3f());
		Vector3f right = _camera.getLocation().getInverseMatrix().positiveX(new Vector3f());
		Vector3f up = new Vector3f(0f, move, 0f);

		backward.y = 0f;
		right.y = 0f;
		backward.normalize().mul(move);
		right.normalize().mul(move);

		if (Input.isKeyHeld("w"))
			pos.sub(backward);
		if (Input.isKeyHeld("s"))
			pos.add(backward);
		if (Input.isKeyHeld("a"))
			pos.sub(right);
		if (Input.isKeyHeld("d"))
			pos.add(right);
		if (Input.isKeyHeld("space"))
			pos.add(up);
		if (Input.isKeyHeld("shift"))
			pos.sub(up);

		_camera.getLocation().setPosition(pos);
	}

	private boolean isValid(Vector3f vec)
	{
		if (Float.isNaN(vec.x))
			return false;
		if (Float.isNaN(vec.y))
			return false;
		if (Float.isNaN(vec.z))
			return false;
		return true;
	}

	private float clamp(float x, float min, float max)
	{
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}

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
