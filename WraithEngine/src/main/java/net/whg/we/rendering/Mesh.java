package net.whg.we.rendering;

import net.whg.we.resources.DisposableResource;

public class Mesh implements DisposableResource
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

	@Override
	public void dispose()
	{
		_vMesh.dispose();
	}

	@Override
	public boolean isDisposed()
	{
		return _vMesh.isDisposed();
	}

	public String getMeshName()
	{
		return _meshName;
	}
}
