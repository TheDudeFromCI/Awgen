package net.whg.we.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.scene.Transform3D;
import net.whg.frameworks.util.MathUtils;
import net.whg.we.legacy.Input;
import net.whg.we.legacy.Screen;
import net.whg.we.legacy.Time;

public class FirstPersonNode extends SceneNode implements UpdateableNode
{
	private static final float MAX_ANGLE = (float) Math.toRadians(89);
	private static final float TAU = (float) Math.PI * 2f;

	// Fields;
	private Vector3f _baseRotation = new Vector3f();
	private Vector3f _extraRotation = new Vector3f();
	private float _mouseSensitivity = 1f;
	private float _moveSpeed = 7f;

	// Temp
	private Matrix4f _matrixBuffer = new Matrix4f();
	private Vector3f _rotationBuffer = new Vector3f();
	private Vector3f _backwardBuffer = new Vector3f();
	private Vector3f _rightBuffer = new Vector3f();
	private Vector3f _upBuffer = new Vector3f();

	@Override
	public void update()
	{
		// if (Input.isKeyDown("escape"))
		// _gameLoop.requestClose();

		/*
		 * TODO Eventually, we should move position into here in order to cleanly work
		 * with physics events.
		 */
	}

	@Override
	public void updateFrame()
	{
		if (Input.isKeyDown("q"))
			Screen.setMouseLocked(!Screen.isMouseLocked());

		setMoveSpeed(Input.isKeyHeld("control") ? 70f : 7f);

		updateCameraRotation();
		updateCameraPosition();
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

		float yaw = Input.getDeltaMouseX() * Time.deltaTime() * getMouseSensitivity();
		float pitch = Input.getDeltaMouseY() * Time.deltaTime() * getMouseSensitivity();

		_rotationBuffer.x = MathUtils.clamp(_baseRotation.x - pitch, -MAX_ANGLE, MAX_ANGLE);
		_rotationBuffer.y = (_baseRotation.y - yaw) % TAU;
		_rotationBuffer.z = _baseRotation.z;

		if (!MathUtils.isValid(_rotationBuffer))
			return;

		_baseRotation.set(_rotationBuffer);
		_rotationBuffer.add(_extraRotation);

		Transform3D transform = (Transform3D) getTransform();
		Quaternionf rot = transform.getRotation();

		rot.identity();
		rot.rotateY(_rotationBuffer.y);
		rot.rotateX(_rotationBuffer.x);
		rot.rotateZ(_rotationBuffer.z);
	}

	private void updateCameraPosition()
	{
		if (!Screen.isMouseLocked())
			return;

		float move = Time.deltaTime() * getMoveSpeed();

		Transform3D transform = (Transform3D) getTransform();
		transform.getInverseMatrix(_matrixBuffer);

		_matrixBuffer.positiveZ(_backwardBuffer);
		_matrixBuffer.positiveX(_rightBuffer);
		_upBuffer.set(0f, move, 0f);

		_backwardBuffer.y = 0f;
		_backwardBuffer.normalize().mul(move);

		_rightBuffer.y = 0f;
		_rightBuffer.normalize().mul(move);

		Vector3f pos = transform.getPosition();
		if (Input.isKeyHeld("w"))
			pos.sub(_backwardBuffer);
		if (Input.isKeyHeld("s"))
			pos.add(_backwardBuffer);
		if (Input.isKeyHeld("a"))
			pos.sub(_rightBuffer);
		if (Input.isKeyHeld("d"))
			pos.add(_rightBuffer);
		if (Input.isKeyHeld("space"))
			pos.add(_upBuffer);
		if (Input.isKeyHeld("shift"))
			pos.sub(_upBuffer);
	}
}
