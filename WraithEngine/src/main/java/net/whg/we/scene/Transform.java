package net.whg.we.scene;

import org.joml.Matrix4f;

public interface Transform
{
	Transform getParent();

	void setParent(Transform transform);

	Matrix4f getLocalMatrix();

	Matrix4f getFullMatrix();
}
