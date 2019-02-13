package net.whg.we.ui.font;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Glyph
{
	private char _c;
	private Vector4f _position;
	private Vector2f _size;
	private Vector2f _offset;
	private float _width;

	public Glyph(char c, Vector4f position, Vector2f size, Vector2f offset, float width)
	{
		_c = c;
		_position = position;
		_size = size;
		_offset = offset;
		_width = width;
	}

	public char getCharacter()
	{
		return _c;
	}

	public Vector4f getPosition()
	{
		return _position;
	}

	public Vector2f getSize()
	{
		return _size;
	}

	public Vector2f getOffset()
	{
		return _offset;
	}

	public float getWidth()
	{
		return _width;
	}
}
