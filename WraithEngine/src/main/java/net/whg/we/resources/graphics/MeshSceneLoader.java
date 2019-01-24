package net.whg.we.resources.graphics;

import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import net.whg.we.rendering.MeshScene;
import net.whg.we.rendering.Skeleton;
import net.whg.we.rendering.VertexData;
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
			// Load the scene file
			AIScene scene = Assimp.aiImportFile(resourceFile.getFile().toString(),
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals
							| Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace
							| Assimp.aiProcess_LimitBoneWeights);

			// If scene could not be loaded, return null
			if (scene == null)
				return FileLoadState.FAILED_TO_LOAD;

			// Count scene information
			int meshCount = scene.mNumMeshes();

			// Load scene meshes
			for (int i = 0; i < meshCount; i++)
			{
				AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
				String meshName = mesh.mName().dataString();
				VertexData vertexData = AssimpMeshParser.loadMesh(mesh);
				Skeleton skeleton = AssimpSkeletonParser.loadSkeleton(scene, mesh, vertexData);

				request.addResource(new MeshResource(meshName, vertexData, skeleton, resourceFile));
			}

			// Cleanup and return
			scene.free();

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
