package net.whg.we.resources;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Texture;
import net.whg.we.rendering.TextureProperties;

public class TextureResource implements Resource<Texture>
{
	private Texture _texture;
	private TextureProperties _properties;
	private ResourceFile _resource;

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
	public ResourceFile getResourceFile()
	{
		return _resource;
	}

	@Override
	public void setResourceFile(ResourceFile resource)
	{
		_resource = resource;
	}

	@Override
	public void dispose()
	{
		if (_texture == null)
			return;

		_texture.dispose();
		_texture = null;
	}
}
