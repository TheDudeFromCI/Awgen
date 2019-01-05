package net.whg.we.resources;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Texture;
import net.whg.we.rendering.TextureProperties;

public class TextureResource implements Resource<Texture>
{
	private Texture _texture;
	private TextureProperties _properties;
	private String _fileName;

	TextureResource(TextureProperties properties)
	{
		_properties = properties;
	}

	@Override
	public Texture getData()
	{
		return _texture;
	}

	public boolean isCompiled()
	{
		return _texture != null;
	}

	public void compile(Graphics graphics)
	{
		_texture = new Texture(graphics.prepareTexture(_properties), _properties);
	}

	@Override
	public String getFileName()
	{
		return _fileName;
	}

	@Override
	public void setFileName(String name)
	{
		_fileName = name;
	}
}
