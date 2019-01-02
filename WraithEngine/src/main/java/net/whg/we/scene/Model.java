package net.whg.we.scene;

import net.whg.we.rendering.LocationHolder;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;
import net.whg.we.utils.Location;

public class Model implements LocationHolder
{
	private SubMesh[] _submeshes;
	private Location _location;

	public Model(Mesh[] meshes, Material[] materials)
	{
		if (meshes.length != materials.length)
			throw new IllegalArgumentException(
					"Mesh array and Material array must be the same length!");

		_submeshes = new SubMesh[meshes.length];
		_location = new Location();

		for (int i = 0; i < _submeshes.length; i++)
			_submeshes[i] = new SubMesh(meshes[i], materials[i], this);
	}

	@Override
	public Location getLocation()
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
}
