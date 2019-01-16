package net.whg.we.scene;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Shader;
import net.whg.we.rendering.Texture;
import net.whg.we.resources.MeshSceneResource;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ShaderResource;
import net.whg.we.resources.TextureResource;

public class SceneLoader
{
	private ResourceLoader _resourceLoader;

	public SceneLoader(ResourceLoader resourceLoader)
	{
		_resourceLoader = resourceLoader;
	}

	public Shader loadShader(ResourceFile resource, Graphics graphics)
	{
		ShaderResource shaderResource = (ShaderResource) _resourceLoader.loadResource(resource);
		shaderResource.compileShader(graphics);
		return shaderResource.getData();
	}

	public Texture loadTexture(ResourceFile resource, Graphics graphics)
	{
		TextureResource textureRes = (TextureResource) _resourceLoader.loadResource(resource);
		textureRes.compile(graphics);
		return textureRes.getData();
	}

	public Model loadModel(ResourceFile resource, Graphics graphics)
	{
		MeshSceneResource scene = (MeshSceneResource) _resourceLoader.loadResource(resource);
		scene.compile(graphics);
		return scene.getData()._models.get(0);
	}
}
