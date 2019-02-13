package net.whg.we.ui;

import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;

public class UIImage implements UIComponent
{
	private boolean _disposed;
	private Mesh _mesh;
	private Material _material;
	private Transform2D _transform = new Transform2D();

	public UIImage(Mesh mesh, Material material)
	{
		_mesh = mesh;
		_material = material;
	}

	@Override
	public Transform2D getTransform()
	{
		return _transform;
	}

	@Override
	public void init()
	{
	}

	@Override
	public void update()
	{
	}

	@Override
	public void updateFrame()
	{
	}

	@Override
	public void render()
	{
		if (_mesh == null || _material == null)
			return;

		_material.bind();
		_material.setOrthoMVP(_transform.getFullMatrix());
		_mesh.render();
	}

	@Override
	public void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
	}

	@Override
	public boolean isDisposed()
	{
		return _disposed;
	}

	public Mesh getMesh()
	{
		return _mesh;
	}

	public Material getMaterial()
	{
		return _material;
	}

	public void setMesh(Mesh mesh)
	{
		_mesh = mesh;
	}

	public void setMaterial(Material material)
	{
		_material = material;
	}
}
