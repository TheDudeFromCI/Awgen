package net.whg.we.client_logic.rendering;

import java.nio.FloatBuffer;

public interface VShader
{
	void recompile(String vert, String geo, String frag);

	void bind();

	boolean isBound();

	void dispose();

	int getShaderId();

	int getUniformLocation(String name);

	void setUniformMat4(int location, FloatBuffer value);

	void setUniformMat4Array(int location, FloatBuffer value);

	void setUniformInt(int location, int value);

	void setUniformVec4(int location, float x, float y, float z, float w);
}
