package net.whg.we.resource;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.util.Arrays;
import net.whg.we.legacy.Color;

public class IntRGBAColorData implements TextureColorData
{
	private static final int FILE_VERSION = 1;

	private int _width;
	private int _height;
	private int[] _rgba;

	public IntRGBAColorData()
	{
		// This constructor exists mostly for Externalize to work correctly.
		this(0, 0);
	}

	public IntRGBAColorData(int width, int height)
	{
		resize(width, height);
	}

	public void resize(int width, int height)
	{
		_width = width;
		_height = height;
		_rgba = new int[width * height];
	}

	@Override
	public int width()
	{
		return _width;
	}

	@Override
	public int height()
	{
		return _height;
	}

	@Override
	public int bytesPerPixel()
	{
		return 4;
	}

	@Override
	public Color getColorAt(int x, int y)
	{
		int pix = _rgba[y * _width + x];
		float r = (pix >> 16 & 0xFF) / 255f;
		float g = (pix >> 8 & 0xFF) / 255f;
		float b = (pix & 0xFF) / 255f;
		float a = (pix >> 24 & 0xFF) / 255f;

		return new Color(r, g, b, a);
	}

	@Override
	public void put(ByteBuffer buffer)
	{
		for (int pix : _rgba)
		{
			buffer.put((byte) (pix >> 16 & 0xFF));
			buffer.put((byte) (pix >> 8 & 0xFF));
			buffer.put((byte) (pix & 0xFF));
			buffer.put((byte) (pix >> 24 & 0xFF));
		}
	}

	public int[] getAsIntArray()
	{
		return _rgba;
	}

	public void setPixel(int x, int y, Color color)
	{
		int pix = 0;
		pix |= Math.round(color.r * 255f) << 16;
		pix |= Math.round(color.g * 255f) << 8;
		pix |= Math.round(color.b * 255f);
		pix |= Math.round(color.a * 255f) << 24;

		_rgba[y * _width + x] = pix;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(FILE_VERSION);
		out.writeInt(_width);
		out.writeInt(_height);
		out.writeObject(_rgba);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		int fileVersion = in.readInt();

		switch (fileVersion) {
			case 1:
				_width = in.readInt();
				_height = in.readInt();
				_rgba = (int[]) in.readObject();
				return;

			default:
				throw new IllegalStateException("Unknown file version: " + fileVersion + "!");
		}
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(_rgba);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof IntRGBAColorData))
			return false;

		IntRGBAColorData o = (IntRGBAColorData) obj;
		return _width == o._width && _height == o._height && Arrays.equals(_rgba, o._rgba);
	}
}
