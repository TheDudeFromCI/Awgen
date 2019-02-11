package net.whg.we.resources.scene;

import java.util.Set;
import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.YamlFile;
import net.whg.we.resources.graphics.ShaderResource;
import net.whg.we.resources.graphics.TextureResource;
import net.whg.we.utils.logging.Log;

public class MaterialLoader implements FileLoader
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
	public MaterialResource loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
	{
		try
		{
			YamlFile yaml = new YamlFile();
			yaml.load(resourceFile.getFile());

			String name = yaml.getString("name");

			ShaderResource shader = (ShaderResource) resourceLoader.loadResource(_fileDatabase
					.getResourceFile(resourceFile.getPlugin(), yaml.getString("shader")), database);

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
					textures[i++] =
							(TextureResource) resourceLoader
									.loadResource(
											_fileDatabase.getResourceFile(resourceFile.getPlugin(),
													yaml.getString("textures", textureName)),
											database);
				}
			}

			MaterialResource material =
					new MaterialResource(resourceFile, name, shader, textureParamNames, textures);
			database.addResource(material);

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
