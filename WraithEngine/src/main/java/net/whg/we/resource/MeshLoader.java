package net.whg.we.resource;

import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class MeshLoader implements FileLoader
{
	private static final String[] FILE_TYPES =
	{
			"asset_mesh"
	};

	private Graphics _graphics;

	public MeshLoader(Graphics graphics)
	{
		_graphics = graphics;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager, ResourceFile resourceFile)
	{
		return new MeshFuture(resourceManager.getFile(resourceFile));
	}

	@Override
	public ResourceData createDataInstace()
	{
		return new MeshData(_graphics);
	}
}
