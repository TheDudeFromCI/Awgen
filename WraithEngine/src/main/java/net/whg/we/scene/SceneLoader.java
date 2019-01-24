package net.whg.we.scene;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Shader;
import net.whg.we.rendering.Texture;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.graphics.ShaderResource;
import net.whg.we.resources.graphics.TextureResource;

public class SceneLoader
{
	private ResourceLoader _resourceLoader;

	public SceneLoader(ResourceLoader resourceLoader)
	{
		_resourceLoader = resourceLoader;
	}

	public Shader loadShader(ResourceFile resource, Graphics graphics)
	{
		ResourceBatchRequest request = new ResourceBatchRequest();
		request.addResourceFile(resource);
		_resourceLoader.loadResources(request);

		ShaderResource shaderResource = (ShaderResource) request.getResource(0);
		shaderResource.compileShader(graphics);
		return shaderResource.getData();
	}

	public Texture loadTexture(ResourceFile resource, Graphics graphics)
	{
		ResourceBatchRequest request = new ResourceBatchRequest();
		request.addResourceFile(resource);
		_resourceLoader.loadResources(request);

		TextureResource textureRes = (TextureResource) request.getResource(0);
		textureRes.compile(graphics);
		return textureRes.getData();
	}

	public Model loadModel(ResourceFile resource, Graphics graphics)
	{
		ResourceBatchRequest request = new ResourceBatchRequest();
		request.addResourceFile(resource);
		_resourceLoader.loadResources(request);

		// SceneResource scene = (SceneResource) request.getResource(0);
		// scene.compile(graphics);
		// return scene.getData()._models.get(0);
		return null;
	}
}
