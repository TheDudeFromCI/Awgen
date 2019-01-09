package net.whg.we.resources;

import java.util.ArrayList;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.MeshScene;
import net.whg.we.scene.Model;
import net.whg.we.utils.Log;

public class MeshSceneResource implements Resource<MeshScene>
{
	private MeshScene _scene;
	private ArrayList<UncompiledMesh> _meshes;
	private ArrayList<UncompiledMaterial> _materials = new ArrayList<>();
	private ArrayList<UncompiledModel> _models = new ArrayList<>();
	private ResourceFile _resource;

	public MeshSceneResource(ArrayList<UncompiledMesh> meshes)
	{
		_meshes = meshes;
	}

	@Override
	public MeshScene getData()
	{
		return _scene;
	}

	public boolean isCompiled()
	{
		return _scene != null;
	}

	public void addMaterial(UncompiledMaterial material)
	{
		_materials.add(material);
	}

	public void addModel(UncompiledModel model)
	{
		_models.add(model);
	}

	private Mesh getMeshByName(MeshScene scene, String name)
	{
		for (Mesh mesh : scene._meshes)
			if (mesh.getName().equals(name))
				return mesh;
		return null;
	}

	private Material getMaterialByName(MeshScene scene, String name)
	{
		for (Material material : scene._materials)
			if (material.getName().equals(name))
				return material;
		return null;
	}

	public void compile(Graphics graphics)
	{
		if (_scene != null)
		{
			Log.warn("Cannot compile mesh scene! Mesh Scene already compiled.");
			return;
		}

		_scene = new MeshScene();

		for (UncompiledMaterial material : _materials)
			_scene._materials.add(material.compile(graphics));

		for (UncompiledMesh mesh : _meshes)
			_scene._meshes.add(mesh.compile(graphics));

		for (UncompiledModel model : _models)
		{
			Log.debugf("Loading submeshes for model %s. SubMesh Count: %s", model.getName(),
					model.getMeshes().length);
			Mesh[] meshes = new Mesh[model.getMeshes().length];
			Material[] materials = new Material[model.getMaterials().length];

			for (int i = 0; i < meshes.length; i++)
			{
				meshes[i] = getMeshByName(_scene, model.getMeshes()[i]);
				materials[i] = getMaterialByName(_scene, model.getMaterials()[i]);
			}

			_scene._models.add(new Model(meshes, materials));
		}

		// Free memory
		_meshes = null;
		_materials = null;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resource;
	}

	@Override
	public void setResourceFile(ResourceFile resource)
	{
		_resource = resource;
	}

	@Override
	public void dispose()
	{
		if (_scene == null)
			return;

		_scene.dispose();
		_scene = null;
	}
}
