package net.whg.we.resources.graphics;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Texture;
import net.whg.we.rendering.TextureProperties;
import net.whg.we.resources.CompilableResource;
import net.whg.we.resources.ResourceFile;

public class TextureResource implements CompilableResource<Texture>
{
	private String _name;
	private Texture _texture;
	private TextureProperties _properties;
	private ResourceFile _resource;

	TextureResource(TextureProperties properties, ResourceFile resource, String name)
	{
		_name = name;
		_properties = properties;
		_resource = resource;
	}

	@Override
	public Texture getData()
	{
		return _texture;
	}

	@Override
	public boolean isCompiled()
	{
		return _texture != null;
	}

	@Override
	public void compile(Graphics graphics)
	{
		if (_texture != null)
			return;

		_texture = new Texture(graphics.prepareTexture(_properties), _properties);
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resource;
	}

	@Override
	public void dispose()
	{
		_resource = null;

		if (_texture != null)
		{
			_texture.dispose();
			_texture = null;
		}
	}

	@Override
	public String getName()
	{
		return _name;
	}
}
