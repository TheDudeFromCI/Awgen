package net.whg.we.resource;

import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class ShaderLoader implements FileLoader
{
	private Graphics _graphics;

	public ShaderLoader(Graphics graphics)
	{
		_graphics = graphics;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return new String[]
		{
			"asset_shader"
		};
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager,
			ResourceFile resourceFile)
	{
		return new ShaderFuture(resourceManager.getFile(resourceFile));
	}

	@Override
	public ResourceData createDataInstace()
	{
		return new ShaderData(_graphics);
	}
}
