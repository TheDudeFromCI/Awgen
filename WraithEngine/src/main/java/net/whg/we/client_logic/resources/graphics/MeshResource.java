package net.whg.we.client_logic.resources.graphics;

import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceState;
import net.whg.we.client_logic.rendering.Graphics;
import net.whg.we.client_logic.rendering.Mesh;
import net.whg.we.client_logic.rendering.Skeleton;
import net.whg.we.client_logic.rendering.SkinnedMesh;
import net.whg.we.client_logic.rendering.VertexData;

public class MeshResource implements Resource
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

	public MeshResource(ResourceFile resourceFile, String name, Mesh mesh)
	{
		_resourceFile = resourceFile;
		_name = name;
		_mesh = mesh;
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

	public void compile(Graphics graphics)
	{
		if (_mesh != null)
			return;

		if (_skeleton == null)
			_mesh = new Mesh(_name, _vertexData, graphics);
		else
			_mesh = new SkinnedMesh(_name, _vertexData, graphics, _skeleton);
	}

	public String getName()
	{
		return _name;
	}

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

	@Override
	public boolean reload()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResourceState getResourceState()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
