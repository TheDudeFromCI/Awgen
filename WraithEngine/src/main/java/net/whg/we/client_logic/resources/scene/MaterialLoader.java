package net.whg.we.client_logic.resources.scene;

import java.util.Set;
import net.whg.frameworks.logging.Log;
import net.whg.frameworks.resource.FileLoader;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.frameworks.resource.ResourceManager;
import net.whg.frameworks.resource.YamlFile;
import net.whg.we.client_logic.resources.graphics.ShaderResource;
import net.whg.we.client_logic.resources.graphics.TextureResource;

public class MaterialLoader implements FileLoader
{
	private static final String[] FILE_TYPES =
	{
			"material"
	};

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public MaterialResource loadFile(ResourceManager resourceManager, ResourceFile resourceFile)
	{
		try
		{
			YamlFile yaml = new YamlFile();
			yaml.load(resourceManager.getFile(resourceFile));

			String name = yaml.getString("name");

			ShaderResource shader = (ShaderResource) resourceManager
					.loadResource(new ResourceFile(yaml.getString("shader")));

			String[] textureParamNames;
			TextureResource[] textures;
			{
				Set<String> textureNames = yaml.getKeys("textures");
				textures = new TextureResource[textureNames.size()];
				textureParamNames = new String[textures.length];

				int i = 0;
				for (String textureName : textureNames)
				{
					textureParamNames[i] = textureName;
					textures[i++] = (TextureResource) resourceManager.loadResource(
							new ResourceFile(yaml.getString("textures", textureName)));
				}
			}

			MaterialResource material =
					new MaterialResource(resourceFile, name, shader, textureParamNames, textures);
			resourceManager.getResourceDatabase().addResource(material);

			Log.debugf("Successfully loaded material resource, %s.", material);
			return material;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to load material %s!", exception, resourceFile);
			return null;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
