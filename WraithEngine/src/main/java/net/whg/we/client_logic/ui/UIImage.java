package net.whg.we.client_logic.ui;

import net.whg.we.client_logic.rendering.Material;
import net.whg.we.client_logic.rendering.Mesh;
import net.whg.we.scene.Transform2D;

public class UIImage implements UIComponent
{
    private Transform2D _transform = new Transform2D();
    private Mesh _mesh;
    private Material _material;
    private boolean _visible = true;
    private boolean _disposed;

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
        if (!_visible)
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

    public boolean isVisible()
    {
        return _visible;
    }

    public void setVisible(boolean visible)
    {
        _visible = visible;
    }
}
