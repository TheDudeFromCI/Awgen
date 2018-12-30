package net.whg.we.resources;

import java.io.File;
import java.util.ArrayList;
import net.whg.we.rendering.MeshScene;

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
	public Resource<MeshScene> loadFile(File file)
	{
		ArrayList<UncompiledMesh> uncompiledMeshes = AssimpSceneParser.load(file);

		if (uncompiledMeshes == null)
			return null;

		return new MeshSceneResource(uncompiledMeshes);
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
