package net.whg.we.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import net.whg.we.rendering.MeshScene;
import net.whg.we.utils.FileUtils;

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
	public Resource<MeshScene> loadFile(File file, AssetProperties assetProperties)
	{
		ArrayList<UncompiledMesh> uncompiledMeshes = AssimpSceneParser.load(file);

		if (uncompiledMeshes == null)
			return null;

		MeshSceneResource scene = new MeshSceneResource(uncompiledMeshes);

		if (assetProperties != null)
			loadDependencies(assetProperties, scene);

		return scene;
	}

	private void loadDependencies(AssetProperties assetProperties, MeshSceneResource scene)
	{
		for (String material : assetProperties.getKeys("materials"))
		{
			String shaderName = assetProperties.getString("materials", material, "shader");
			File shader = FileUtils.getResource(assetProperties.getPlugin(), shaderName);
			UncompiledMaterial mat =
					new UncompiledMaterial(assetProperties.getPlugin(), material, shader);

			Set<String> textureList = assetProperties.getKeys("materials", material, "textures");
			File[] textureFiles = new File[textureList.size()];

			int textureIndex = 0;
			for (String textureId : textureList)
			{
				String textureName =
						assetProperties.getString("materials", material, "textures", textureId);
				textureFiles[textureIndex++] =
						FileUtils.getResource(assetProperties.getPlugin(), textureName);
			}

			mat.setTextures(textureFiles);
			scene.addMaterial(mat);
		}

		for (String model : assetProperties.getKeys("models"))
		{
			String[] meshes;
			String[] materials;

			ArrayList<String> meshList = new ArrayList<>();
			ArrayList<String> materialList = new ArrayList<>();

			for (String submeshId : assetProperties.getKeys("models", model))
			{
				meshList.add(assetProperties.getString("models", model, submeshId, "mesh"));
				materialList.add(assetProperties.getString("models", model, submeshId, "material"));
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
