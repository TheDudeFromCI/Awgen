package net.whg.we.rendering;

public class ShaderAttributes
{
	private String[] _attribNames;
	private int[] _attribSizes;
	private int _count;
	private int _vertexSize;

	public ShaderAttributes()
	{
		this(4);
	}

	public ShaderAttributes(int bufferSize)
	{
		_attribNames = new String[bufferSize];
		_attribSizes = new int[bufferSize];
	}

	public int getCount()
	{
		return _count;
	}

	public int getVertexSize()
	{
		return _vertexSize;
	}

	public String getAttributeName(int index)
	{
		if (index < 0 || index >= _count)
			throw new ArrayIndexOutOfBoundsException(
					"Index " + index + " out of bounds! (Size: " + _count + ")");
		return _attribNames[index];
	}

	public int getAttributeSize(int index)
	{
		if (index < 0 || index >= _count)
			throw new ArrayIndexOutOfBoundsException(
					"Index " + index + " out of bounds! (Size: " + _count + ")");
		return _attribSizes[index];
	}

	public void addAttribute(String name, int size)
	{
		if (size <= 0)
			throw new IllegalArgumentException("Attribute sizes must be a positive integer!");
		if (size > 4)
			throw new IllegalArgumentException("Attribute sizes must 4 or lower!");

		if (_count + 1 >= _attribNames.length)
		{
			String[] newNames = new String[_attribNames.length + 4];
			int[] newSizes = new int[newNames.length];

			for (int i = 0; i < _attribNames.length; i++)
			{
				newNames[i] = _attribNames[i];
				newSizes[i] = _attribSizes[i];
			}

			_attribNames = newNames;
			_attribSizes = newSizes;
		}

		_attribNames[_count] = name;
		_attribSizes[_count] = size;

		_count++;
		_vertexSize += size;
	}

	public void removeAttribute(int index)
	{
		if (index < 0 || index >= _count)
			throw new ArrayIndexOutOfBoundsException(
					"Index " + index + " out of bounds! (Size: " + _count + ")");

		_vertexSize -= _attribSizes[index];
		for (int i = index; i < _count; i++)
		{
			_attribNames[i] = _attribNames[i + 1];
			_attribSizes[i] = _attribSizes[i + 1];
		}

		_count--;
	}

	public int getVertexByteSize()
	{
		return getVertexSize() * 4;
	}

	public int getSizeOf(String attrib)
	{
		int index = indexOf(attrib);

		if (index == -1)
			return -1;

		return _attribSizes[index];
	}

	public int indexOf(String attrib)
	{
		for (int i = 0; i < _count; i++)
			if (_attribNames[i].equals(attrib))
				return i;
		return -1;
	}

	public int getPositionInVertex(String attribute)
	{
		return getPositionInVertex(indexOf(attribute));
	}

	public int getPositionInVertex(int attributeIndex)
	{
		if (attributeIndex == -1)
			return -1;

		int t = 0;
		for (int i = 0; i < attributeIndex; i++)
			t += _attribSizes[i];
		return t;
	}
}
