package net.whg.we.rendering;

import net.whg.we.utils.logging.Log;

public class Mesh
{
	private String _meshName;
	private VMesh _vMesh;

	public Mesh(String meshName, VertexData vertexData, Graphics graphics)
	{
		_meshName = meshName;
		_vMesh = graphics.prepareMesh(vertexData);
	}

	public void render()
	{
		_vMesh.render();
	}

	public void dispose()
	{
		_vMesh.dispose();
		Log.tracef("Disposed mesh '%s'.", _meshName);
	}

	public boolean isDisposed()
	{
		return _vMesh.isDisposed();
	}

	public String getName()
	{
		return _meshName;
	}
}
