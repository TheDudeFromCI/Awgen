package net.whg.we.client_logic.scene;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.frameworks.util.MathUtils;
import net.whg.we.client_logic.rendering.Camera;
import net.whg.we.client_logic.utils.Input;
import net.whg.we.client_logic.utils.Screen;
import net.whg.we.scene.Location;
import net.whg.we.utils.Time;

/**
 * An instance of a PersonCamera. In this case the camera is first person and
 * the updating methods are constructed given this.
 */
public class FirstPersonCamera implements PlayerController
{
	private static final float MAX_ANGLE = (float) Math.toRadians(89);
	private static final float TAU = (float) Math.PI * 2f;

	private Camera _camera;
	private Vector3f _baseRotation;
	private Vector3f _extraRotation;
	private float _mouseSensitivity = .4f;
	private float _moveSpeed = 7f;
	private WindowedGameLoop _gameLoop;

	private Vector3f _rotationBuffer = new Vector3f();
	private Quaternionf _rotationStorageBuffer = new Quaternionf();

	public FirstPersonCamera(WindowedGameLoop gameLoop)
	{
		_camera = new Camera();
		_baseRotation = new Vector3f();
		_extraRotation = new Vector3f();
		_gameLoop = gameLoop;
	}

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

	@Override
	public void updateFrame()
	{
		setMoveSpeed(Input.isKeyHeld("control") ? 70f : 7f);

		if (Input.isKeyDown("q"))
			Screen.setMouseLocked(!Screen.isMouseLocked());
		if (Input.isKeyDown("escape"))
			_gameLoop.requestClose();

		updateCameraPosition();
		updateCameraRotation();
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

	public Location getLocation()
	{
		return _camera.getLocation();
	}

	@Override
	public Camera getCamera()
	{
		return _camera;
	}
}
