package net.whg.we.resources.scene;

import net.whg.we.resources.FileLoadState;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.YamlFile;
import net.whg.we.scene.Scene;
import net.whg.we.utils.Log;

public class SceneLoader implements FileLoader<Scene>
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
	public FileLoadState loadFile(ResourceBatchRequest request, ResourceFile resourceFile)
	{
		try
		{
			boolean allDependenciesLoaded = false;

			YamlFile yamlFile = new YamlFile();
			yamlFile.load(resourceFile.getFile());

			for (@SuppressWarnings("unused")
			String models : yamlFile.getKeys("models"))
			{
				// TODO
			}

			if (allDependenciesLoaded)
				return FileLoadState.LOADED_SUCCESSFULLY;
			return FileLoadState.PUSH_TO_BACK;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to load resource %s!", exception, resourceFile);
			return FileLoadState.FAILED_TO_LOAD;
		}
	}
}
