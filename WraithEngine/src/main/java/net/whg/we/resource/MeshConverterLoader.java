package net.whg.we.resource;

import java.util.UUID;
import net.whg.frameworks.external.AssimpAPI;
import net.whg.frameworks.external.AssimpAPIBridge;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceData;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceFuture;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.we.client_logic.rendering.Graphics;

public class MeshConverterLoader implements FileLoader
{
	private Graphics _graphics;
	private AssimpAPI _assimp;

	public MeshConverterLoader(Graphics graphics)
	{
		_graphics = graphics;
		_assimp = new AssimpAPIBridge();
	}

	public MeshConverterLoader(Graphics graphics, AssimpAPI assimp)
	{
		_graphics = graphics;
		_assimp = assimp;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return new String[]
		{
			"fbx", "obj", "dae", "gltf", "glb", "blend", "3ds", "ase", "ifc", "xgl", "zgl", "ply", "lwo", "lws", "lxo",
			"stl", "x", "ac", "ms3d"
		};
	}

	@Override
	public ResourceFuture loadFile(ResourceManager resourceManager, ResourceFile resourceFile)
	{
		String destFolder = resourceFile.getPathname().replace('.', '_');
		return new MeshConverterFuture(_graphics, resourceManager, resourceManager.getFile(resourceFile), destFolder,
				_assimp);
	}

	@Override
	public ResourceData createDataInstace(UUID uuid)
	{
		return new ConverterData(uuid);
	}
}
