package net.whg.frameworks.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import net.whg.frameworks.logging.Log;

public class ByteWriter
{
	private OutputStream _outputStream;
	private byte[] _bytes;
	private int _pos;

	public ByteWriter(byte[] buffer)
	{
		_bytes = buffer;
		_pos = 0;
	}

	public ByteWriter(OutputStream outputStream)
	{
		_outputStream = outputStream;
		_pos = 0;
	}

	public ByteWriter writeByte(int value)
	{
		byte b = (byte) (value & 0xFF);

		if (_outputStream != null)
			try
			{
				_outputStream.write(b);
			}
			catch (IOException e)
			{
				Log.errorf("Failed to write byte!", e);
			}
		else
			_bytes[_pos++] = b;

		return this;
	}

	public ByteWriter writeBool(boolean value)
	{
		writeByte(value ? 1 : 0);

		return this;
	}

	public ByteWriter writeShort(int value)
	{
		writeByte(value >> 8);
		writeByte(value);

		return this;
	}

	public ByteWriter writeInt(int value)
	{
		writeByte(value >> 24);
		writeByte(value >> 16);
		writeByte(value >> 8);
		writeByte(value);

		return this;
	}

	public ByteWriter writeLong(long value)
	{
		writeByte((int) (value >> 56L));
		writeByte((int) (value >> 48L));
		writeByte((int) (value >> 40L));
		writeByte((int) (value >> 32L));
		writeByte((int) (value >> 24L));
		writeByte((int) (value >> 16L));
		writeByte((int) (value >> 8L));
		writeByte((int) value);

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
		if (_outputStream != null)
			try
			{
				_outputStream.write(bytes, pos, length);
			}
			catch (IOException e)
			{
				Log.errorf("Failed to write bytes!", e);
			}
		else
		{
			for (int i = 0; i < length; i++)
				_bytes[_pos++] = bytes[i + pos];
		}

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
