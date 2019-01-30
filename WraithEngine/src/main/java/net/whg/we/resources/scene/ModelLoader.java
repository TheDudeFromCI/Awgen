package net.whg.we.resources.scene;

import java.util.Set;
import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.YamlFile;
import net.whg.we.resources.graphics.MeshResource;
import net.whg.we.utils.Log;

public class ModelLoader implements FileLoader
{
	private static final String[] FILE_TYPES =
	{
			"model"
	};

	private FileDatabase _fileDatabase;

	public ModelLoader(FileDatabase fileDatabase)
	{
		_fileDatabase = fileDatabase;
	}

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public ModelResource loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
	{
		try
		{
			YamlFile yaml = new YamlFile();
			yaml.load(resourceFile.getFile());

			String name = yaml.getString("name");
			MeshResource[] meshes;
			MaterialResource[] materials;

			{
				Set<String> submeshes = yaml.getKeys("submeshes");
				meshes = new MeshResource[submeshes.size()];
				materials = new MaterialResource[submeshes.size()];

				int i = 0;
				for (String submesh : submeshes)
				{
					Log.debugf("Loading model %s dependencices. (Mesh: %s, Material: %s)",
							resourceFile, yaml.getString("submeshes", submesh, "mesh"),
							yaml.getString("submeshes", submesh, "material"));

					meshes[i] =
							(MeshResource) resourceLoader.loadResource(
									_fileDatabase.getResourceFile(resourceFile.getPlugin(),
											yaml.getString("submeshes", submesh, "mesh")),
									database);

					materials[i] =
							(MaterialResource) resourceLoader.loadResource(
									_fileDatabase.getResourceFile(resourceFile.getPlugin(),
											yaml.getString("submeshes", submesh, "material")),
									database);

					i++;
				}
			}

			ModelResource model = new ModelResource(resourceFile, name, meshes, materials);
			database.addResource(model);

			Log.debugf("Successfully loaded model resource, %s.", model);
			return model;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to load model %s!", exception, resourceFile);
			return null;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
