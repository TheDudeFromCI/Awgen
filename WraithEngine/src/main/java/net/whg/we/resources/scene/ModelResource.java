package net.whg.we.resources.scene;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.resources.CompilableResource;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.graphics.MeshResource;
import net.whg.we.scene.Model;

public class ModelResource implements CompilableResource
{
	private Model _model;
	private ResourceFile _resourceFile;
	private String _name;
	private MeshResource[] _meshResources;
	private MaterialResource[] _materialResources;

	public ModelResource(ResourceFile resourceFile, String name, MeshResource[] meshResources,
			MaterialResource[] materialResources)
	{
		_resourceFile = resourceFile;
		_name = name;
		_meshResources = meshResources;
		_materialResources = materialResources;
	}

	@Override
	public Model getData()
	{
		return _model;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	@Override
	public void dispose()
	{
		_model = null;
		_meshResources = null;
		_materialResources = null;
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public boolean isCompiled()
	{
		return _model != null;
	}

	@Override
	public void compile(Graphics graphics)
	{
		if (_model != null)
			return;

		Mesh[] meshes;
		Material[] materials;

		{
			meshes = new Mesh[_meshResources.length];
			for (int i = 0; i < meshes.length; i++)
			{
				if (!_meshResources[i].isCompiled())
					_meshResources[i].compile(graphics);
				meshes[i] = _meshResources[i].getData();
			}
		}

		{
			materials = new Material[_materialResources.length];
			for (int i = 0; i < materials.length; i++)
			{
				if (!_materialResources[i].isCompiled())
					_materialResources[i].compile(graphics);
				materials[i] = _materialResources[i].getData();
			}
		}

		_model = new Model(_name, meshes, materials);

		_meshResources = null;
		_materialResources = null;
	}

	@Override
	public String toString()
	{
		return String.format("[ModelResource: %s at %s]", _name, _resourceFile);
	}
}
