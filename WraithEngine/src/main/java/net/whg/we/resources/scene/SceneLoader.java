package net.whg.we.resources.scene;

import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;

public class SceneLoader implements FileLoader
{
	private static final String[] FILE_TYPES =
	{
			"scene"
	};

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
	public Resource loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
	{
		return null;
	}
}
