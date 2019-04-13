package net.whg.we.resource;

import net.whg.frameworks.resource.ResourceData;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.ShaderAttributes;
import net.whg.we.client_logic.rendering.VMesh;
import net.whg.we.client_logic.rendering.VertexData;

public class MeshData implements ResourceData
{
	private VertexData _vertexData;
	private VMesh _vMesh;

	public MeshData(Graphics graphics)
	{
		emptyMeshData();
		_vMesh = graphics.prepareMesh(_vertexData);
	}

	public MeshData(Graphics graphics, VertexData vertexData)
	{
		if (vertexData == null)
			emptyMeshData();
		else
			_vertexData = vertexData;

		_vMesh = graphics.prepareMesh(_vertexData);
	}

	private void emptyMeshData()
	{
		_vertexData = new VertexData(new float[0], new short[0], new ShaderAttributes());
	}

	@Override
	public void dispose()
	{
		_vMesh.dispose();
	}

	public VertexData getVertexData()
	{
		return _vertexData;
	}

	public void setVertexData(VertexData vertexData)
	{
		_vertexData = vertexData;

		if (_vertexData == null)
			emptyMeshData();

		updateVMesh();
	}

	public void updateVMesh()
	{
		_vMesh.rebuild(_vertexData);
	}

	public void render()
	{
		_vMesh.render();
	}
}
