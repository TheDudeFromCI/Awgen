package net.whg.we.scene;

import java.io.File;
import net.whg.we.main.Plugin;
import net.whg.we.rendering.Graphics;
import net.whg.we.rendering.Shader;
import net.whg.we.rendering.Texture;
import net.whg.we.resources.MeshSceneResource;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ShaderResource;
import net.whg.we.resources.TextureResource;
import net.whg.we.utils.FileUtils;

public class SceneUtils
{
	public static Shader loadShader(Plugin plugin, String asset, Graphics graphics)
	{
		File shaderFile = FileUtils.getResource(plugin, asset);
		ShaderResource shaderResource =
				(ShaderResource) ResourceLoader.loadResource(plugin, shaderFile);
		shaderResource.compileShader(graphics);
		Shader shader = shaderResource.getData();

		return shader;
	}

	public static Texture loadTexture(Plugin plugin, String asset, Graphics graphics)
	{
		TextureResource textureRes = (TextureResource) ResourceLoader.loadResource(plugin,
				FileUtils.getResource(plugin, asset));
		textureRes.compile(graphics);
		return textureRes.getData();
	}

	public static Model loadModel(Plugin plugin, String asset, Graphics graphics)
	{
		MeshSceneResource scene = (MeshSceneResource) ResourceLoader.loadResource(plugin,
				FileUtils.getResource(plugin, asset));

		scene.compile(graphics);

		return scene.getData()._models.get(0);
	}
}
