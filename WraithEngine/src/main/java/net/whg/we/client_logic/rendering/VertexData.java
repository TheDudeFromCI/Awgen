package net.whg.we.client_logic.rendering;

public class VertexData
{
    private float[] _data;
    private short[] _triangles;
    private ShaderAttributes _attributes;

    public VertexData(float[] data, short[] triangles,
            ShaderAttributes attributes)
    {
        _data = data;
        _triangles = triangles;
        _attributes = attributes;
    }

    public float[] getDataArray()
    {
        return _data;
    }

    public int getVertexSize()
    {
        return _attributes.getVertexSize();
    }

    public int getVertexByteSize()
    {
        return _attributes.getVertexByteSize();
    }

    public short[] getTriangles()
    {
        return _triangles;
    }

    public int getTriangleCount()
    {
        return _triangles.length;
    }

    public int getVertexCount()
    {
        return _data.length / getVertexSize();
    }

    public ShaderAttributes getAttributeSizes()
    {
        return _attributes;
    }
}
