package net.whg.we.resources.graphics;

import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import net.whg.we.rendering.Mesh;
import net.whg.we.rendering.Skeleton;
import net.whg.we.rendering.VertexData;
import net.whg.we.resources.FileLoader;
import net.whg.we.resources.Resource;
import net.whg.we.resources.ResourceDatabase;
import net.whg.we.resources.ResourceFile;
import net.whg.we.resources.ResourceLoader;
import net.whg.we.utils.Log;

/**
 * Loads a mesh scene file. These can contain either a single mesh, or multiple
 * meshes,
 *
 * @author TheDudeFromCI
 */
public class MeshSceneLoader implements FileLoader<Mesh>
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
	public Resource<Mesh> loadFile(ResourceLoader resourceLoader, ResourceDatabase database,
			ResourceFile resourceFile)
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
				return null;

			// Count scene information
			int meshCount = scene.mNumMeshes();

			MeshResource mainMesh = null;

			// Load scene meshes
			for (int i = 0; i < meshCount; i++)
			{
				AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
				String meshName = mesh.mName().dataString();
				VertexData vertexData = AssimpMeshParser.loadMesh(mesh);
				Skeleton skeleton = AssimpSkeletonParser.loadSkeleton(scene, mesh, vertexData);

				MeshResource resource =
						new MeshResource(meshName, vertexData, skeleton, resourceFile);
				database.addResource(resource);

				if (resource.getName().equals(resourceFile.getName()) || meshCount == 1)
					mainMesh = resource;
			}

			scene.free();

			return mainMesh;
		}
		catch (Exception exception)
		{
			Log.errorf("Failed to load mesh resource %s!", exception, resourceFile);
			return null;
		}
	}

	@Override
	public int getPriority()
	{
		return 0;
	}
}
