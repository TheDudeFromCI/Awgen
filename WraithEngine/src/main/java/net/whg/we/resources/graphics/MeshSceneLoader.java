package net.whg.we.resources.graphics;

import java.util.ArrayList;
import java.util.Set;
import net.whg.we.rendering.MeshScene;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.resources.YamlFile;

/**
 * Loads a mesh scene file. These can contain either a single mesh, or multiple
 * meshes,
 *
 * @author TheDudeFromCI
 */
public class MeshSceneLoader implements FileLoader<MeshScene>
{
	private static final String[] FILE_TYPES =
	{
			"fbx", "obj", "dae", "gltf", "glb", "blend", "3ds", "ase", "ifc", "xgl", "zgl", "ply",
			"lwo", "lws", "lxo", "stl", "x", "ac", "ms3d"
	};

	@Override
	public String[] getTargetFileTypes()
	{
		return FILE_TYPES;
	}

	@Override
	public Resource<MeshScene> loadFile(ResourceLoader resourceLoader, ResourceFile resource)
	{
		ArrayList<UncompiledMesh> uncompiledMeshes = AssimpSceneParser.load(resource.getFile());

		if (uncompiledMeshes == null)
			return null;

		MeshSceneResource scene = new MeshSceneResource(uncompiledMeshes);

		if (resource.getPropertiesFile() != null)
			loadDependencies(resourceLoader, resource, scene);

		return scene;
	}

	private void loadDependencies(ResourceLoader resourceLoader, ResourceFile resource,
			MeshSceneResource scene)
	{
		YamlFile yaml = new YamlFile();
		yaml.load(resource.getPropertiesFile());

		for (String material : yaml.getKeys("materials"))
		{
			String shaderName = yaml.getString("materials", material, "shader");
			ResourceFile shaderResource = resourceLoader.getFileDatabase()
					.getResourceFile(resource.getPlugin(), shaderName);

			ResourceBatchRequest request = new ResourceBatchRequest();
			request.addResourceFile(shaderResource);
			resourceLoader.loadResources(request);

			ShaderResource shader = (ShaderResource) request.getResource(0);
			UncompiledMaterial mat = new UncompiledMaterial(material, shader);

			Set<String> textureList = yaml.getKeys("materials", material, "textures");
			ResourceFile[] textureFiles = new ResourceFile[textureList.size()];

			int textureIndex = 0;
			for (String textureId : textureList)
			{
				String textureName = yaml.getString("materials", material, "textures", textureId);
				textureFiles[textureIndex++] = resourceLoader.getFileDatabase()
						.getResourceFile(resource.getPlugin(), textureName);
			}

			TextureResource[] textures = new TextureResource[textureFiles.length];
			for (int i = 0; i < textures.length; i++)
			{
				request = new ResourceBatchRequest();
				request.addResourceFile(textureFiles[i]);
				resourceLoader.loadResources(request);

				textures[i] = (TextureResource) request.getResource(0);
			}
			mat.setTextures(textures);
			scene.addMaterial(mat);
		}

		for (String model : yaml.getKeys("models"))
		{
			String[] meshes;
			String[] materials;

			ArrayList<String> meshList = new ArrayList<>();
			ArrayList<String> materialList = new ArrayList<>();

			for (String submeshId : yaml.getKeys("models", model))
			{
				meshList.add(yaml.getString("models", model, submeshId, "mesh"));
				materialList.add(yaml.getString("models", model, submeshId, "material"));
			}

			meshes = new String[meshList.size()];
			materials = new String[materialList.size()];

			for (int i = 0; i < meshes.length; i++)
			{
				meshes[i] = meshList.get(i);
				materials[i] = materialList.get(i);
			}

			scene.addModel(new UncompiledModel(model, meshes, materials));
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
