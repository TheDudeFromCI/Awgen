package net.whg.frameworks.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import net.whg.frameworks.logging.Log;

public class ByteReader
{
	private InputStream _inputStream;
	private byte[] _bytes;
	private int _pos;

	public ByteReader(byte[] buffer)
	{
		_bytes = buffer;
		_pos = 0;
	}

	public ByteReader(InputStream inputStream)
	{
		_inputStream = inputStream;
	}

	public byte getByte()
	{
		if (_inputStream != null)
			try
			{
				_pos++;
				return (byte) _inputStream.read();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to read byte!", e);
				return 0;
			}

		return _bytes[_pos++];
	}

	public boolean getBool()
	{
		return getByte() != 0;
	}

	public short getShort()
	{
		int value = 0;

		value |= (getByte() & 0xFF) << 8;
		value |= getByte() & 0xFF;

		return (short) value;
	}

	public int getInt()
	{
		int value = 0;

		value |= (getByte() & 0xFF) << 24;
		value |= (getByte() & 0xFF) << 16;
		value |= (getByte() & 0xFF) << 8;
		value |= getByte() & 0xFF;

		return value;
	}

	public long getLong()
	{
		long value = 0;

		value |= (getByte() & 0xFF) << 56L;
		value |= (getByte() & 0xFF) << 48L;
		value |= (getByte() & 0xFF) << 40L;
		value |= (getByte() & 0xFF) << 32L;
		value |= (getByte() & 0xFF) << 24L;
		value |= (getByte() & 0xFF) << 16L;
		value |= (getByte() & 0xFF) << 8L;
		value |= getByte() & 0xFF;

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
		_pos += length;

		if (_inputStream != null)
		{
			try
			{
				_inputStream.read(bytes, pos, length);
			}
			catch (IOException e)
			{
				Log.errorf("Failed to read byte!", e);
			}
			return;
		}

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
		if (_inputStream != null)
			try
			{
				return _inputStream.available();
			}
			catch (IOException e)
			{
				Log.errorf("Failed to estimate remaining bytes!", e);
				return 0;
			}

		return _bytes.length;
	}

	public int getRemainingBytes()
	{
		return getCapacity() - getPos();
	}

	public void setPos(int pos)
	{
		if (_inputStream != null)
			throw new IllegalStateException("Operation not supported for input streams!");

		_pos = pos;
	}
}
