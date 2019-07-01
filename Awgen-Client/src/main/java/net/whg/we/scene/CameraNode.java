package net.whg.we.scene;

import org.joml.Matrix4f;
import net.whg.frameworks.scene.SceneNode;
import net.whg.we.legacy.Screen;

public class CameraNode extends SceneNode
{
	private Matrix4f _projectionMatrix = new Matrix4f();
	private float _fov = (float) Math.toRadians(70f);
	private float _nearClip = 0.1f;
	private float _farClip = 1000f;

	public CameraNode()
	{
		rebuildProjectionMatrix();
	}

	private void rebuildProjectionMatrix()
	{
		float aspect = (float) Screen.width() / Screen.height();

		_projectionMatrix.identity();
		_projectionMatrix.perspective(_fov, aspect, _nearClip, _farClip);
	}

	public Matrix4f getProjectionMatrix()
	{
		return _projectionMatrix;
	}

	public float getFov()
	{
		return _fov;
	}

	public void setFov(float radians)
	{
		_fov = radians;

		rebuildProjectionMatrix();
	}

	public float getNearClip()
	{
		return _nearClip;
	}

	public float getFarClip()
	{
		return _farClip;
	}

	public void setClippingDistance(float near, float far)
	{
		_nearClip = near;
		_farClip = far;

		rebuildProjectionMatrix();
	}

	@Override
	public String getNodeType()
	{
		return "player.camera";
	}
}
