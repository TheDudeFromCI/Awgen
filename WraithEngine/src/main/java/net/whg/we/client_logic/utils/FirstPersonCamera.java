package net.whg.we.client_logic.utils;

import org.joml.Vector3f;
import net.whg.we.client_logic.rendering.Camera;
import net.whg.we.utils.MathUtils;
import net.whg.we.utils.Time;

/**
 * An instance of a PersonCamera. In this case the camera is first person and
 * the updating methods are constructed given this.
 */
public class FirstPersonCamera extends PersonCamera
{
	public FirstPersonCamera(Camera camera)
	{
		_camera = camera;
		_baseRotation = new Vector3f();
		_extraRotation = new Vector3f();
	}

	@Override
	public void updateCameraRotation()
	{
		if (!Screen.isMouseLocked())
			return;

		float yaw = Input.getDeltaMouseX() * Time.deltaTime() * getMouseSensitivity();
		float pitch = Input.getDeltaMouseY() * Time.deltaTime() * getMouseSensitivity();

		_rotationBuffer.x = MathUtils.clamp(_baseRotation.x - pitch, -MAX_ANGLE, MAX_ANGLE);
		_rotationBuffer.y = (_baseRotation.y - yaw) % TAU;
		_rotationBuffer.z = _baseRotation.z;

		if (!MathUtils.isValid(_rotationBuffer))
			return;

		_baseRotation.set(_rotationBuffer);
		_rotationBuffer.add(_extraRotation);

		_rotationStorageBuffer.identity();
		_rotationStorageBuffer.rotateY(_rotationBuffer.y);
		_rotationStorageBuffer.rotateX(_rotationBuffer.x);
		_rotationStorageBuffer.rotateZ(_rotationBuffer.z);
		_camera.getLocation().setRotation(_rotationStorageBuffer);
	}

	@Override
	public void updateCameraPosition()
	{
		if (!Screen.isMouseLocked())
			return;

		float move = Time.deltaTime() * getMoveSpeed();
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
}
