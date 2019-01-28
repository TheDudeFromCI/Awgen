package net.whg.we.resources.scene;

import java.util.Set;
import net.whg.we.rendering.Material;
import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
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
	public Resource<Material> loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
	{
		try
		{
			YamlFile yaml = new YamlFile();
			yaml.load(resourceFile.getFile());

			ShaderResource shader = (ShaderResource) resourceLoader.loadResource(_fileDatabase
					.getResourceFile(resourceFile.getPlugin(), yaml.getString("shader")), database);

			TextureResource[] textures;
			{
				Set<String> textureNames = yaml.getKeys("textures");
				textures = new TextureResource[textureNames.size()];

				int i = 0;
				for (String textureName : textureNames)
				{
					textures[i++] =
							(TextureResource) resourceLoader
									.loadResource(
											_fileDatabase.getResourceFile(resourceFile.getPlugin(),
													yaml.getString("textures", textureName)),
											database);
				}
			}

			MaterialResource material =
					new MaterialResource(resourceFile.getName(), shader, textures);
			database.addResource(material);

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
