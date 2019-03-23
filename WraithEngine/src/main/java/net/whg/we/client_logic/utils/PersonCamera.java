package net.whg.we.client_logic.utils;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import net.whg.we.client_logic.rendering.Camera;
import net.whg.we.scene.Location;

/**
 * This abstract class represents a camera in game which will be updated when
 * the character moves. It can be implemented to be first person or third
 * person. It contains all the setters and getters, the Camera Rotation and
 * Position have to be defined by the inheriting classes.
 */
public abstract class PersonCamera
{

    protected static final float MAX_ANGLE = (float) Math.toRadians(89);
    protected static final float TAU = (float) Math.PI * 2f;

    protected Camera _camera;
    protected Vector3f _baseRotation;
    protected Vector3f _extraRotation;
    private float _mouseSensitivity = .4f;
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

    /**
     * This sets the Camera Rotation by updating the vectors rotationBuffer and
     * rotationStorageBuffer. The updating method will depend on the view. The
     * mouse movement, speed and sensitivity are taken into account to update
     * the vectors.
     */
    public abstract void updateCameraRotation();

    /**
     * This updates the camera position given the player's moves. The updating
     * method will depend on the view.
     */
    public abstract void updateCameraPosition();

    public Location getLocation()
    {
        return _camera.getLocation();
    }

    /**
     * This method calls the two methods used to update the camera's position
     * and rotation.
     */
    public void update()
    {
        updateCameraRotation();
        updateCameraPosition();
    }
}
