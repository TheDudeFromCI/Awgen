package net.whg.we.render;

import java.nio.FloatBuffer;
import net.whg.we.client_logic.rendering.VShader;
import net.whg.we.resource.UncompiledShader;

/**
 * Represents a headless shader implementation. This class has no internal
 * functionality.
 *
 * @author TheDudeFromCI
 */
public class HeadlessShader implements VShader
{
	@Override
	public void recompile(UncompiledShader shader)
	{
	}

	@Override
	public void bind()
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public int getShaderId()
	{
		return 0;
	}

	@Override
	public int getUniformLocation(String name)
	{
		return 0;
	}

	@Override
	public void setUniformMat4(int location, FloatBuffer value)
	{
	}

	@Override
	public void setUniformMat4Array(int location, FloatBuffer value)
	{
	}

	@Override
	public void setUniformInt(int location, int value)
	{
	}

	@Override
	public void setUniformVec4(int location, float x, float y, float z, float w)
	{
	}
}
