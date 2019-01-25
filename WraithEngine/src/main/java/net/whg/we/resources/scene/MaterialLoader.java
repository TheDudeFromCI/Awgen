package net.whg.we.resources.scene;

import java.util.ArrayList;
import java.util.List;
import net.whg.we.rendering.Material;
import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.FileLoadState;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceDependencies;
import net.whg.we.resources.ResourceDependency;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.YamlFile;
import net.whg.we.resources.graphics.ShaderResource;
import net.whg.we.resources.graphics.TextureResource;
import net.whg.we.utils.Log;

public class MaterialLoader implements FileLoader<Material>
{
	private static final String[] FILE_TYPES =
	{
			"material"
	};

	private FileDatabase _fileDatabase;

	public MaterialLoader(FileDatabase fileDatabase)
	{
		_fileDatabase = fileDatabase;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public FileLoadState loadFile(ResourceBatchRequest request, ResourceFile resourceFile)
	{
		try
		{
			ResourceDependencies dependencies = request.getResourceDependencies(resourceFile);

			// Check if all dependencies have been queued.
			if (dependencies.getDependencyCount() == 0)
			{
				YamlFile yaml = new YamlFile();
				yaml.load(resourceFile.getFile());

				String shaderPath = yaml.getString("shader");
				ResourceFile shaderResource =
						_fileDatabase.getResourceFile(resourceFile.getPlugin(), shaderPath);
				ResourceDependency sub = new ResourceDependency(shaderResource, "shader");
				dependencies.addDependency(sub);

				for (String textureName : yaml.getKeys("textures"))
				{
					String texturePath = yaml.getString("textures", textureName);
					ResourceFile textureResource =
							_fileDatabase.getResourceFile(resourceFile.getPlugin(), texturePath);
					sub = new ResourceDependency(textureResource, "texture." + textureName);
					dependencies.addDependency(sub);
				}

				return FileLoadState.PUSH_TO_BACK;
			}

			// Check if all dependencies have been loaded.
			if (!dependencies.isFullyLoaded(request))
				return FileLoadState.PUSH_TO_BACK;

			ShaderResource shader;
			{
				ResourceDependency shaderDependency = dependencies.getDependency("shader");
				ResourceFile shaderResourceFile = shaderDependency.getResourceFile();
				shader = (ShaderResource) request.getResource(shaderResourceFile);
			}

			TextureResource[] textures;
			{
				List<ResourceDependency> textureDependencyList = new ArrayList<>();
				dependencies.searchDependencies(textureDependencyList, "");
				textures = new TextureResource[textureDependencyList.size()];
				for (int i = 0; i < textures.length; i++)
				{
					ResourceDependency t = textureDependencyList.get(i);
					textures[i] = (TextureResource) request.getResource(t.getResourceFile());
				}
			}

			MaterialResource material =
					new MaterialResource(resourceFile.getName(), shader, textures);

			request.addResource(material);

			return FileLoadState.LOADED_SUCCESSFULLY;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to load material %s!", exception, resourceFile);
			return FileLoadState.FAILED_TO_LOAD;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
