package net.whg.we.resource;

import java.nio.ByteBuffer;
import net.whg.we.legacy.Color;

public class IntRGBAColorData implements TextureColorData
{
	private int _width;
	private int _height;
	private int[] _rgba;

	public IntRGBAColorData(int width, int height)
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
}
