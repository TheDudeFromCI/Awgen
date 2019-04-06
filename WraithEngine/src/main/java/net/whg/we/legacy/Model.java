package net.whg.we.legacy;

import net.whg.frameworks.scene.Transform3D;
import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.rendering.Mesh;

public class Model
{
	private String _name;
	private SubMesh[] _submeshes;
	private Transform3D _location;

	public Model(String name, Mesh[] meshes, Material[] materials)
	{
		if (meshes.length != materials.length)
			throw new IllegalArgumentException(
					"Mesh array and Material array must be the same length!");

		_name = name;

		_submeshes = new SubMesh[meshes.length];
		_location = new Transform3D();

		for (int i = 0; i < _submeshes.length; i++)
			_submeshes[i] = new SubMesh(meshes[i], materials[i], this);
	}

	public Transform3D getLocation()
	{
		return _location;
	}

	public int getSubMeshCount()
	{
		return _submeshes.length;
	}

	public SubMesh getSubMesh(int index)
	{
		return _submeshes[index];
	}

	public String getName()
	{
		return _name;
	}

	public void setLocation(Transform3D location)
	{
		_location = location;
	}
}
