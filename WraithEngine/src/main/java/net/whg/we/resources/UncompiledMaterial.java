package net.whg.we.resources;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Texture;

public class UncompiledMaterial
{
	private String _name;
	private ShaderResource _shader;
	private TextureResource[] _textures;

	public UncompiledMaterial(String name, ResourceFile shaderResource)
	{
		_name = name;
		_shader = (ShaderResource) ResourceLoader.loadResource(shaderResource);
	}

	public void setTextures(ResourceFile... textureResources)
	{
		_textures = new TextureResource[textureResources.length];
		for (int i = 0; i < _textures.length; i++)
			_textures[i] = (TextureResource) ResourceLoader.loadResource(textureResources[i]);
	}

	public Material compile(Graphics graphics)
	{
		if (!_shader.isCompiled())
			_shader.compileShader(graphics);

		Material material = new Material(_shader.getData(), _name);

		if (_textures != null)
		{
			Texture[] tex = new Texture[_textures.length];
			for (int i = 0; i < _textures.length; i++)
			{
				if (!_textures[i].isCompiled())
					_textures[i].compile(graphics);
				tex[i] = _textures[i].getData();
			}

			material.setTextures(tex);
		}

		return material;
	}

	public String getName()
	{
		return _name;
	}
}
