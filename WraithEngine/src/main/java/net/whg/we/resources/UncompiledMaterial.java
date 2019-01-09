package net.whg.we.resources;

import java.io.File;
import net.whg.we.main.Plugin;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Material;
import net.whg.we.rendering.Texture;

public class UncompiledMaterial
{
	private Plugin _plugin;
	private String _name;
	private ShaderResource _shader;
	private TextureResource[] _textures;

	public UncompiledMaterial(Plugin plugin, String name, File shaderResource)
	{
		_plugin = plugin;
		_name = name;
		_shader = (ShaderResource) ResourceLoader.loadResource(_plugin, shaderResource);
	}

	public void setTextures(File... textureResources)
	{
		_textures = new TextureResource[textureResources.length];
		for (int i = 0; i < _textures.length; i++)
			_textures[i] =
					(TextureResource) ResourceLoader.loadResource(_plugin, textureResources[i]);
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
