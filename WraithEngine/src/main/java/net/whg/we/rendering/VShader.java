package net.whg.we.rendering;

import java.nio.FloatBuffer;

public interface VShader
{
	public void bind();

	public boolean isBound();

	public void dispose();

	public int getShaderId();

	public int getUniformLocation(String name);

	public void setUniformMat4(int location, FloatBuffer value);

	public void setUniformMat4Array(int location, FloatBuffer value);

	public void setUniformInt(int location, int value);

	public void setUniformVec4(int location, float x, float y, float z, float w);
}
