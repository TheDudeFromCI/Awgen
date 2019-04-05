package net.whg.we.client_logic.rendering;

import net.whg.frameworks.logging.Log;

public class Mesh
{
    private String _meshName;
    private VMesh _vMesh;

    public Mesh(String meshName, VertexData vertexData, Graphics graphics)
    {
        _meshName = meshName;
        _vMesh = graphics.prepareMesh(vertexData);
    }

    public void render()
    {
        _vMesh.render();
    }

    public void dispose()
    {
        _vMesh.dispose();
        Log.tracef("Disposed mesh '%s'.", _meshName);
    }

    public boolean isDisposed()
    {
        return _vMesh.isDisposed();
    }

    public String getName()
    {
        return _meshName;
    }

    public void rebuild(VertexData vertexData)
    {
        _vMesh.rebuild(vertexData);
    }
}
