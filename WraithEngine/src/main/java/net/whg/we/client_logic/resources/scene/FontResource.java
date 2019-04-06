package net.whg.we.client_logic.resources.scene;

import net.whg.frameworks.resource.Resource;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.we.client_logic.ui.font.Font;

public class FontResource implements Resource
{
	private ResourceFile _resourceFile;
	private String _name;
	private Font _font;

	public FontResource(ResourceFile resourceFile, String name, Font font)
	{
		_resourceFile = resourceFile;
		_name = name;
		_font = font;
	}

	@Override
	public Font getData()
	{
		return _font;
	}

	@Override
	public ResourceFile getResourceFile()
	{
		return _resourceFile;
	}

	@Override
	public void dispose()
	{
		_font = null;
	}

	@Override
	public String getName()
	{
		return _name;
	}
}
