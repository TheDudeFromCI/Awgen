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

	@Override
	public String[] getTargetFileTypes()
	{
		return new String[]
		{
				"fbx", "obj", "dae", "gltf", "glb", "blend", "3ds", "ase",
				"ifc", "xgl", "zgl", "ply", "lwo", "lws", "lxo", "stl", "x",
				"ac", "ms3d"
		};
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager,
			ResourceFile resourceFile)
	{
		/*
		 * Because all resources must be within the local resource database in
		 * order to be loaded, imported file must be dropped in there first.
		 * After converting them, they can be safely deleted. A local copy can
		 * also be kept incase import settings need to be adjusted.
		 */

		String destFolder = resourceFile.getPathname().replace('.', '_');
		return new MeshConverterFuture(_graphics,
				resourceManager.getResourceDatabase(),
				resourceManager.getFile(resourceFile), destFolder);
	}

	@Override
	public ResourceData createDataInstace()
	{
		return new ConverterData();
	}
}
