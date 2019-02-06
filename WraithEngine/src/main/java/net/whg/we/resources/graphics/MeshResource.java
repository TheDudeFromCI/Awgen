package net.whg.we.resources.graphics;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.Skeleton;
import net.whg.we.rendering.SkinnedMesh;
import net.whg.we.rendering.VertexData;
import net.whg.we.resources.CompilableResource;
import net.whg.we.resources.ResourceFile;

public class MeshResource implements CompilableResource
{
	private Mesh _mesh;
	private String _name;
	private VertexData _vertexData;
	private Skeleton _skeleton;
	private ResourceFile _resourceFile;

	public MeshResource(ResourceFile resourceFile, String name, VertexData vertexData,
			Skeleton skeleton)
	{
		_resourceFile = resourceFile;
		_name = name;
		_vertexData = vertexData;
		_skeleton = skeleton;
	}

	@Override
	public Mesh getData()
	{
		return _mesh;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	@Override
	public void dispose()
	{
		if (_mesh != null)
		{
			_mesh.dispose();
			_mesh = null;
		}

		_vertexData = null;
		_skeleton = null;
	}

	@Override
	public void compile(Graphics graphics)
	{
		if (_mesh != null)
			return;

		if (_skeleton == null)
			_mesh = new Mesh(_name, _vertexData, graphics);
		else
			_mesh = new SkinnedMesh(_name, _vertexData, graphics, _skeleton);
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public boolean isCompiled()
	{
		return _mesh != null;
	}

	@Override
	public String toString()
	{
		return String.format("[MeshResource: %s at %s]", _name, _resourceFile);
	}

	public VertexData getVertexData()
	{
		return _vertexData;
	}

	public Skeleton getSkeleton()
	{
		return _skeleton;
	}
}
