package net.whg.we.scene;

import net.whg.we.gamelogic.Location;
import net.whg.we.rendering.LocationHolder;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.Renderable;
import whg.WraithEngine.rendering.Camera;
import whg.WraithEngine.rendering.Material;

public class Model implements Renderable, LocationHolder
{
	private Mesh _mesh;
	private Location _location;
	private Material _material;

	public Model(Mesh mesh, Material material)
	{
		_mesh = mesh;
		_material = material;
		_location = new Location();
	}

	@Override
	public Location getLocation()
	{
		return _location;
	}

	@Override
	public void render(Camera camera)
	{
		_material.bind();
		_material.setMVPUniform(camera, _location);
		_mesh.render();
	}

	public Mesh getMesh()
	{
		return _mesh;
	}

	public Material getMaterial()
	{
		return _material;
	}

	public void setMaterial(Material material)
	{
		_material = material;
	}
}
