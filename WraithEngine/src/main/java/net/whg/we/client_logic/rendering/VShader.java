package net.whg.we.client_logic.rendering;

import java.nio.FloatBuffer;
import net.whg.we.resource.UncompiledShader;

public interface VShader
{
	void recompile(UncompiledShader shader);

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
