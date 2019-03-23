package net.whg.we.client_logic.rendering;

import org.joml.Matrix4f;
import net.whg.we.client_logic.utils.Screen;
import net.whg.we.scene.Location;

public class Camera
{
    private Matrix4f _projectionMatrix;
    private float _fov = (float) Math.toRadians(70f);
    private float _nearClip = 0.1f;
    private float _farClip = 1000f;
    private Location _location;

    public Camera()
    {
        _projectionMatrix = new Matrix4f();
        _location = new Location();

        rebuildProjectionMatrix();
    }

    public void rebuildProjectionMatrix()
    {
        _projectionMatrix.identity();

        float aspect = (float) Screen.width() / Screen.height();
        _projectionMatrix.perspective(_fov, aspect, _nearClip, _farClip);
    }

    public Location getLocation()
    {
        return _location;
    }

    public Matrix4f getProjectionMatrix(Matrix4f buffer)
    {
        buffer.set(_projectionMatrix);
        return buffer;
    }

    public Matrix4f getViewMatrix(Matrix4f buffer)
    {
        _location.getMatrix(buffer);
        buffer.invert();
        return buffer;
    }
}
