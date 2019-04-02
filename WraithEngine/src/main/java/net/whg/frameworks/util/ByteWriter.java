package net.whg.frameworks.util;

import java.nio.charset.Charset;

public class ByteWriter
{
	private byte[] _bytes;
	private int _pos;

	public ByteWriter(byte[] buffer)
	{
		_bytes = buffer;
		_pos = 0;
	}

	public ByteWriter writeByte(int value)
	{
		_bytes[_pos++] = (byte) (value & 0xFF);

		return this;
	}

	public ByteWriter writeShort(int value)
	{
		_bytes[_pos++] = (byte) (value >> 8 & 0xFF);
		_bytes[_pos++] = (byte) (value & 0xFF);

		return this;
	}

	public ByteWriter writeInt(int value)
	{
		_bytes[_pos++] = (byte) (value >> 24 & 0xFF);
		_bytes[_pos++] = (byte) (value >> 16 & 0xFF);
		_bytes[_pos++] = (byte) (value >> 8 & 0xFF);
		_bytes[_pos++] = (byte) (value & 0xFF);

		return this;
	}

	public ByteWriter writeLong(long value)
	{
		_bytes[_pos++] = (byte) (value >> 56L & 0xFF);
		_bytes[_pos++] = (byte) (value >> 48L & 0xFF);
		_bytes[_pos++] = (byte) (value >> 40L & 0xFF);
		_bytes[_pos++] = (byte) (value >> 32L & 0xFF);
		_bytes[_pos++] = (byte) (value >> 24L & 0xFF);
		_bytes[_pos++] = (byte) (value >> 16L & 0xFF);
		_bytes[_pos++] = (byte) (value >> 8L & 0xFF);
		_bytes[_pos++] = (byte) (value & 0xFF);

		return this;
	}

	public ByteWriter writeFloat(float value)
	{
		writeInt(Float.floatToIntBits(value));

		return this;
	}

	public ByteWriter writeDouble(double value)
	{
		writeLong(Double.doubleToLongBits(value));

		return this;
	}

	public ByteWriter writeBytes(byte[] bytes, int pos, int length)
	{
		for (int i = 0; i < length; i++)
			_bytes[_pos++] = bytes[i + pos];

		return this;
	}

	public ByteWriter writeBytes(byte[] bytes)
	{
		writeBytes(bytes, 0, bytes.length);

		return this;
	}

	public ByteWriter writeString(String value, Charset encoding)
	{
		byte[] bytes = value.getBytes(encoding);
		writeInt(bytes.length);
		writeBytes(bytes);

		return this;
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
