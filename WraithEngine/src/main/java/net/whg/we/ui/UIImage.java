package net.whg.we.ui;

import org.joml.Vector2f;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Mesh;

public class UIImage implements UIComponent
{
	private Vector2f _pos = new Vector2f();
	private Vector2f _size = new Vector2f();
	private boolean _disposed;
	private Mesh _mesh;
	private Material _material;

	public UIImage(Mesh mesh, Material material)
	{
		_mesh = mesh;
		_material = material;
	}

	@Override
	public Vector2f getPosition()
	{
		return _pos;
	}

	@Override
	public Vector2f getSize()
	{
		return _size;
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
		_material.bind();
		_material.setMVPUniform(_pos, _size);
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
