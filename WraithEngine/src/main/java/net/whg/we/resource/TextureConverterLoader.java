package net.whg.we.resource;

import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class TextureConverterLoader implements FileLoader
{
	private Graphics _graphics;

	public TextureConverterLoader(Graphics graphics)
	{
		_graphics = graphics;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return new String[]
		{
			"png", "jpg", "gif", "bmp", "jpeg"

		};
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager,
			ResourceFile resourceFile)
	{
		String destFolder = resourceFile.getPathname().replace('.', '_');
		return new TextureConverterFuture(_graphics, resourceManager,
				resourceManager.getFile(resourceFile), destFolder);
	}

	@Override
	public ResourceData createDataInstace()
	{
		return new ConverterData();
	}
}
