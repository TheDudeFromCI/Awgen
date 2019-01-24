package net.whg.we.resources.scene;

import java.util.ArrayList;
import net.whg.we.rendering.Material;
import net.whg.we.resources.FileDatabase;
import net.whg.we.resources.FileLoadState;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceDependencies;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.SubResourceFile;
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

	private SubResourceFile getShader(ResourceDependencies dependencies)
	{
		for (SubResourceFile sub : dependencies.getDependencies())
			if (sub.getResourceType() == ShaderResource.class)
				return sub;
		return null;
	}

	private ArrayList<SubResourceFile> getTextures(ResourceDependencies dependencies)
	{
		ArrayList<SubResourceFile> subs = new ArrayList<>();

		for (SubResourceFile sub : dependencies.getDependencies())
			if (sub.getResourceType() == TextureResource.class)
				subs.add(sub);

		return subs;
	}

	@Override
	public FileLoadState loadFile(ResourceBatchRequest request, ResourceFile resourceFile)
	{
		try
		{
			ResourceDependencies dependencies = request.getResourceDependencies(resourceFile);

			if (dependencies.getDependencyCount() == 0)
			{
				YamlFile yaml = new YamlFile();
				yaml.load(resourceFile.getFile());

				String shaderPath = yaml.getString("shader");
				ResourceFile shaderResource =
						_fileDatabase.getResourceFile(resourceFile.getPlugin(), shaderPath);
				SubResourceFile sub = new SubResourceFile(shaderResource, shaderResource.getName(),
						ShaderResource.class);
				dependencies.addDependency(sub);

				for (String textureName : yaml.getKeys("textures"))
				{
					String texturePath = yaml.getString("textures", textureName);
					ResourceFile textureResource =
							_fileDatabase.getResourceFile(resourceFile.getPlugin(), texturePath);
					sub = new SubResourceFile(textureResource, textureName, TextureResource.class);
					dependencies.addDependency(sub);
				}

				return FileLoadState.PUSH_TO_BACK;
			}

			SubResourceFile shaderRes = getShader(dependencies);
			ShaderResource shader = (ShaderResource) request
					.getResource(shaderRes.getResourceFile(), shaderRes.getName());

			ArrayList<SubResourceFile> textureRes = getTextures(dependencies);
			TextureResource[] textures = new TextureResource[textureRes.size()];
			for (int i = 0; i < textures.length; i++)
			{
				SubResourceFile t = textureRes.get(i);
				textures[i] =
						(TextureResource) request.getResource(t.getResourceFile(), t.getName());
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
