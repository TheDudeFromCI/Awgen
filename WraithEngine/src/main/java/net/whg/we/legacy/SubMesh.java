package net.whg.we.legacy;

import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.rendering.Mesh;

public class SubMesh
{
	private Mesh _mesh;
	private Material _material;
	private Model _model;

	public SubMesh(Mesh mesh, Material material, Model model)
	{
		_mesh = mesh;
		_material = material;
		_model = model;
	}

	public Mesh getMesh()
	{
		return _mesh;
	}

	public Material getMaterial()
	{
		return _material;
	}

	public Model getModel()
	{
		return _model;
	}

	public void setMaterial(Material material)
	{
		_material = material;
	}

	public void setMesh(Mesh mesh)
	{
		_mesh = mesh;
	}
}
