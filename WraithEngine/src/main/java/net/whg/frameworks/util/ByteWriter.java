package net.whg.frameworks.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

	public ByteWriter writeByte(byte b)
	{
		if (_outputStream != null)
			try
			{
				_outputStream.write(b);
				_pos++;
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
		writeByte((byte) (value ? 1 : 0));

		return this;
	}

	public ByteWriter writeShort(short value)
	{
		writeByte((byte) (value >> 8));
		writeByte((byte) value);

		return this;
	}

	public ByteWriter writeInt(int value)
	{
		writeByte((byte) (value >> 24));
		writeByte((byte) (value >> 16));
		writeByte((byte) (value >> 8));
		writeByte((byte) value);

		return this;
	}

	public ByteWriter writeLong(long value)
	{
		writeByte((byte) (value >> 56));
		writeByte((byte) (value >> 48));
		writeByte((byte) (value >> 40));
		writeByte((byte) (value >> 32));
		writeByte((byte) (value >> 24));
		writeByte((byte) (value >> 16));
		writeByte((byte) (value >> 8));
		writeByte((byte) value);

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
				_pos += length;
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
		if (_outputStream != null)
			return -1;

		return _bytes.length;
	}

	public int getRemainingBytes()
	{
		if (_outputStream != null)
			return -1;

		return getCapacity() - getPos();
	}

	public void setPos(int pos)
	{
		if (_outputStream != null)
			throw new IllegalStateException(
					"Operation not supported for output streams!");

		_pos = pos;
	}

	public void writeObject(Object obj)
	{
		try
		{
			if (_outputStream == null)
			{
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				new ObjectOutputStream(stream).writeObject(obj);
				stream.close();

				writeBytes(stream.toByteArray());
			}
			else
				new ObjectOutputStream(_outputStream).writeObject(obj);
		}
		catch (IOException e)
		{
			Log.errorf("Failed to write bytes!", e);
		}
	}
}
