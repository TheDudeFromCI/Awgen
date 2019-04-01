package net.whg.frameworks.scene;

import org.joml.Matrix4f;

public interface Transform
{
	void getLocalMatrix(Matrix4f out);

	void getFullMatrix(Matrix4f parent, Matrix4f out);
}
