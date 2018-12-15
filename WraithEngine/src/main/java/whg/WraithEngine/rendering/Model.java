package whg.WraithEngine.rendering;

import whg.WraithEngine.gamelogic.Entity;
import whg.WraithEngine.gamelogic.Location;

public class Model implements Entity, Renderable
{
	private Mesh _mesh;
	private Location _location;
	private Material _material;

	public Model(Mesh mesh, Location location, Material material)
	{
		_mesh = mesh;
		_location = new Location(location);
		_material = material;
	}

	public Mesh getMesh()
	{
		return _mesh;
	}

	public Location getLocation()
	{
		return _location;
	}

	public Material getMaterial()
	{
		return _material;
	}

	@Override
	public void render(Camera camera)
	{
		_material.bind();
		_material.setMVPUniform(camera, _location);
		_mesh.render();
	}
}
