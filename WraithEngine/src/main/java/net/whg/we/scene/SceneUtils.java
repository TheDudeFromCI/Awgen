package net.whg.we.scene;

import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Shader;
import net.whg.we.rendering.Texture;
import net.whg.we.resources.MeshSceneResource;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ShaderResource;
import net.whg.we.resources.TextureResource;

public class SceneUtils
{
	public static Shader loadShader(ResourceFile resource, Graphics graphics)
	{
		ShaderResource shaderResource = (ShaderResource) ResourceLoader.loadResource(resource);
		shaderResource.compileShader(graphics);
		return shaderResource.getData();
	}

	public static Texture loadTexture(ResourceFile resource, Graphics graphics)
	{
		TextureResource textureRes = (TextureResource) ResourceLoader.loadResource(resource);
		textureRes.compile(graphics);
		return textureRes.getData();
	}

	public static Model loadModel(ResourceFile resource, Graphics graphics)
	{
		MeshSceneResource scene = (MeshSceneResource) ResourceLoader.loadResource(resource);
		scene.compile(graphics);
		return scene.getData()._models.get(0);
	}
}
