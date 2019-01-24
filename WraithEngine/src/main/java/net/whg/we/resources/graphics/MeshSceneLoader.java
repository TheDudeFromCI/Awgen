package net.whg.we.resources.graphics;

import java.util.ArrayList;
import net.whg.we.rendering.MeshScene;
import net.whg.we.resources.FileLoadState;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.ResourceBatchRequest;
import net.whg.we.resources.ResourceFile;
import net.whg.we.utils.Log;

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
	public FileLoadState loadFile(ResourceBatchRequest request, ResourceFile resourceFile)
	{
		try
		{
			ArrayList<UncompiledMesh> uncompiledMeshes =
					AssimpSceneParser.load(resourceFile.getFile());

			if (uncompiledMeshes == null)
				return FileLoadState.FAILED_TO_LOAD;

			for (UncompiledMesh m : uncompiledMeshes)
				request.addResource(new MeshResource(m.getName(), m.getVertexData(),
						m.getSkeleton(), resourceFile));

			return FileLoadState.LOADED_SUCCESSFULLY;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to load mesh resource %s!", exception, resourceFile);
			return FileLoadState.FAILED_TO_LOAD;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
