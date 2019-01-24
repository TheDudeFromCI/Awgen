package net.whg.we.resources.graphics;

public class UncompiledModel
{
	private String _name;
	private String[] _mesh;
	private String[] _material;

	public UncompiledModel(String name, String[] mesh, String[] material)
	{
		_name = name;
		_mesh = mesh;
		_material = material;
	}

	public String[] getMeshes()
	{
		return _mesh;
	}

	public String[] getMaterials()
	{
		return _material;
	}

	public String getName()
	{
		return _name;
	}
}
