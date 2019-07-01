package net.whg.we.render;

import net.whg.we.client_logic.rendering.VMesh;
import net.whg.we.client_logic.rendering.VertexData;

/**
 * Represents a headless mesh implementation. This class has no internal
 * functionality.
 * 
 * @author TheDudeFromCI
 */
public class HeadlessMesh implements VMesh
{
	private boolean _disposed;

	@Override
	public void render()
	{
	}

	@Override
	public void dispose()
	{
		_disposed = true;
	}

	@Override
	public void rebuild(VertexData vertexData)
	{
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}
}
