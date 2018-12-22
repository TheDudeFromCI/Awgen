package net.whg.we.rendering;

public class VertexData
{
	private float[] _data;
	private short[] _triangles;
	private int[] _attributes;
	private int _vertexSize;

	public VertexData(float[] data, short[] triangles, int[] attributes)
	{
		_data = data;
		_triangles = triangles;
		_attributes = attributes;

		_vertexSize = 0;
		for (int i : _attributes)
			_vertexSize += i;
	}

	public float[] getDataArray()
	{
		return _data;
	}

	public int getVertexSize()
	{
		return _vertexSize;
	}

	public int getVertexByteSize()
	{
		return _vertexSize * 4;
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
		return _data.length / _vertexSize;
	}

	public int[] getAttributeSizes()
	{
		return _attributes;
	}
}
