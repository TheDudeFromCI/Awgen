package net.whg.we.resources.scene;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Texture;
import net.whg.we.resources.CompilableResource;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.graphics.ShaderResource;
import net.whg.we.resources.graphics.TextureResource;

public class MaterialResource implements CompilableResource
{
	private Material _material;
	private String _name;
	private ShaderResource _shader;
	private TextureResource[] _textures;
	private ResourceFile _resource;
	private boolean _compiled;

	public MaterialResource(String name, ShaderResource shader, TextureResource[] textures)
	{
		_name = name;
		_shader = shader;
		_textures = textures;
	}

	@Override
	public Material getData()
	{
		return _material;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resource;
	}

	@Override
	public void dispose()
	{
		_material = null;
		_shader = null;
		_textures = null;
	}

	@Override
	public void compile(Graphics graphics)
	{
		if (_compiled)
			return;

		if (!_shader.isCompiled())
			_shader.compile(graphics);

		for (TextureResource tex : _textures)
			if (!tex.isCompiled())
				tex.compile(graphics);

		Texture[] textures = new Texture[_textures.length];

		for (int i = 0; i < textures.length; i++)
			textures[i] = _textures[i].getData();

		_material = new Material(_shader.getData(), _name);
		_material.setTextures(textures);

		_compiled = true;

		_shader = null;
		_textures = null;
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public boolean isCompiled()
	{
		return _compiled;
	}
}
