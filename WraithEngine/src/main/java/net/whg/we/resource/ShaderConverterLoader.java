package net.whg.we.resource;

import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class ShaderConverterLoader implements FileLoader
{
	private Graphics _graphics;

	public ShaderConverterLoader(Graphics graphics)
	{
		_graphics = graphics;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return new String[]
		{
			"glsl"
		};
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager,
			ResourceFile resourceFile)
	{
		String destFolder = resourceFile.getPathname().replace('.', '_');
		return new ShaderConverterFuture(_graphics, resourceManager,
				resourceManager.getFile(resourceFile), destFolder);
	}

	@Override
	public ResourceData createDataInstace()
	{
		return new ConverterData();
	}
}
