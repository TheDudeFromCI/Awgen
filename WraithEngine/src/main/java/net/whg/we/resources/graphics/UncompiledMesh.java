package net.whg.we.resources.graphics;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.Skeleton;
import net.whg.we.rendering.SkinnedMesh;
import net.whg.we.rendering.VertexData;

public class UncompiledMesh
{
	private VertexData _vertexData;
	private String _name;
	private Skeleton _skeleton;

	public UncompiledMesh(String name, VertexData vertexData)
	{
		_name = name;
		_vertexData = vertexData;
	}

	public Mesh compile(Graphics graphics)
	{
		if (_skeleton == null)
			return new Mesh(_name, _vertexData, graphics);
		return new SkinnedMesh(_name, _vertexData, graphics, _skeleton);
	}

	public VertexData getVertexData()
	{
		return _vertexData;
	}

	public String getName()
	{
		return _name;
	}

	public Skeleton getSkeleton()
	{
		return _skeleton;
	}

	public void setSkeleton(Skeleton skeleton)
	{
		_skeleton = skeleton;
	}
}
