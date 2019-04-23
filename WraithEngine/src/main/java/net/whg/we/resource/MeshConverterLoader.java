package net.whg.we.resource;

import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class MeshConverterLoader implements FileLoader
{
	private Graphics _graphics;

	public MeshConverterLoader(Graphics graphics)
	{
		_graphics = graphics;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return new String[]
		{
			"fbx", "obj", "dae", "gltf", "glb", "blend", "3ds", "ase", "ifc",
			"xgl", "zgl", "ply", "lwo", "lws", "lxo", "stl", "x", "ac", "ms3d"
		};
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager,
			ResourceFile resourceFile)
	{
		String destFolder = resourceFile.getPathname().replace('.', '_');
		return new MeshConverterFuture(_graphics, resourceManager,
				resourceManager.getFile(resourceFile), destFolder);
	}

	@Override
	public ResourceData createDataInstace()
	{
		return new ConverterData();
	}
}
