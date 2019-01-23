package net.whg.we.resources.scene;

import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.ResourceFile;
import net.whg.we.scene.Scene;

public class SceneLoader implements FileLoader<Scene>
{
	private static final String[] FILE_TYPES = { "scene" };

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public int getPriority()
	{
		return 0;
	}

	@Override
	public Resource<Scene> loadFile(ResourceLoader resourceLoader, ResourceFile resourceFile)
	{
		return null;
	}
}
