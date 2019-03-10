package net.whg.we.utils;

import java.nio.charset.Charset;

public class ByteReader
{
	private byte[] _bytes;
	private int _pos;

	public ByteReader(byte[] buffer)
	{
		_bytes = buffer;
		_pos = 0;
	}

	public byte getByte()
	{
		return _bytes[_pos++];
	}

	public short getShort()
	{
		int value = 0;

		value |= (_bytes[_pos++] & 0xFF) << 8;
		value |= _bytes[_pos++] & 0xFF;

		return (short) value;
	}

	public int getInt()
	{
		int value = 0;

		value |= (_bytes[_pos++] & 0xFF) << 24;
		value |= (_bytes[_pos++] & 0xFF) << 16;
		value |= (_bytes[_pos++] & 0xFF) << 8;
		value |= _bytes[_pos++] & 0xFF;

		return value;
	}

	public long getLong()
	{
		long value = 0;

		value |= (_bytes[_pos++] & 0xFF) << 56L;
		value |= (_bytes[_pos++] & 0xFF) << 48L;
		value |= (_bytes[_pos++] & 0xFF) << 40L;
		value |= (_bytes[_pos++] & 0xFF) << 32L;
		value |= (_bytes[_pos++] & 0xFF) << 24L;
		value |= (_bytes[_pos++] & 0xFF) << 16L;
		value |= (_bytes[_pos++] & 0xFF) << 8L;
		value |= _bytes[_pos++] & 0xFF;

		return value;
	}

	public float getFloat()
	{
		return Float.intBitsToFloat(getInt());
	}

	public double getDouble()
	{
		return Double.longBitsToDouble(getLong());
	}

	public void getBytes(byte[] bytes, int pos, int length)
	{
		for (int i = 0; i < length; i++)
			bytes[i + pos] = getByte();
	}

	public void getBytes(byte[] bytes)
	{
		getBytes(bytes, 0, bytes.length);
	}

	public String getString(Charset encoding)
	{
		int length = getInt();
		byte[] bytes = new byte[length];
		getBytes(bytes);

		return new String(bytes, encoding);
	}

	public int getPos()
	{
		return _pos;
	}

	public byte[] getBytes()
	{
		return _bytes;
	}

	public int getCapacity()
	{
		return _bytes.length;
	}

	public int getRemainingBytes()
	{
		return getCapacity() - getPos();
	}

	public void setPos(int pos)
	{
		_pos = pos;
	}
}
