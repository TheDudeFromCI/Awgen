package net.whg.we.client_logic.resources.scene;

import java.util.Set;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.YamlFile;
import net.whg.we.client_logic.resources.graphics.MeshResource;

public class ModelLoader implements FileLoader
{
	private static final String[] FILE_TYPES =
	{
			"model"
	};

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public ModelResource loadFile(ResourceManager resourceManager, ResourceFile resourceFile)
	{
		try
		{
			YamlFile yaml = new YamlFile();
			yaml.load(resourceManager.getFile(resourceFile));

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

					meshes[i] = (MeshResource) resourceManager.loadResource(
							new ResourceFile(yaml.getString("submeshes", submesh, "mesh")));

					materials[i] = (MaterialResource) resourceManager.loadResource(
							new ResourceFile(yaml.getString("submeshes", submesh, "material")));

					i++;
				}
			}

			ModelResource model = new ModelResource(resourceFile, name, meshes, materials);
			resourceManager.getResourceDatabase().addResource(model);

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
