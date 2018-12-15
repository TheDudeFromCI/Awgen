package whg.WraithEngine.gamelogic;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import whg.WraithEngine.core.Input;
import whg.WraithEngine.core.Time;
import whg.WraithEngine.rendering.Camera;

public class FirstPersonCamera implements Entity, Updateable
{
	private static final float MAX_ANGLE = (float) Math.toRadians(89);
	private static final float TAU = (float) Math.PI * 2f;

	private Camera _camera;
	private Vector3f _baseRotation;
	private Vector3f _extraRotation;
	private float _mouseSensitivity = 10f;

	private Vector3f _rotationBuffer = new Vector3f();
	private Quaternionf _rotationStorageBuffer = new Quaternionf();

	public FirstPersonCamera(Camera camera)
	{
		_camera = camera;
		_baseRotation = new Vector3f();
		_extraRotation = new Vector3f();
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
		float yaw =
				Input.getDeltaMouseX() * Time.deltaTime() * _mouseSensitivity;
		float pitch =
				Input.getDeltaMouseY() * Time.deltaTime() * _mouseSensitivity;

		_rotationBuffer.x =
				clamp(_baseRotation.x - pitch, -MAX_ANGLE, MAX_ANGLE);
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
		Vector3f velocity = new Vector3f();
		float movementSpeed = 7f;

		if (Input.isKeyHeld("w"))
			velocity.z -= 1f;
		if (Input.isKeyHeld("s"))
			velocity.z += 1f;
		if (Input.isKeyHeld("a"))
			velocity.x -= 1f;
		if (Input.isKeyHeld("d"))
			velocity.x += 1f;

		if (velocity.lengthSquared() != 0f)
		{
			velocity.normalize();
			velocity.mulDirection(_camera.getLocation().getMatrix());
		}

		velocity.y = 0f;
		if (Input.isKeyHeld("space"))
			velocity.y += 1f;
		if (Input.isKeyHeld("shift"))
			velocity.y -= 1f;

		if (velocity.lengthSquared() == 0f)
			return;

		velocity.mul(Time.deltaTime()).mul(movementSpeed);

		Vector3f pos = _camera.getLocation().getPosition();
		pos.add(velocity);
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

	@Override
	public Location getLocation()
	{
		return _camera.getLocation();
	}

	@Override
	public void update()
	{
		updateCameraPosition();
		updateCameraRotation();
	}
}
