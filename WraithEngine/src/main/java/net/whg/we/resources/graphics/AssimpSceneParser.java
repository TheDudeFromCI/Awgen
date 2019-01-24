package net.whg.we.resources.graphics;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

class AssimpSceneParser
{
	static ArrayList<UncompiledMesh> load(File file)
	{
		// Load the scene file
		AIScene scene = Assimp.aiImportFile(file.toString(),
				Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals
						| Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace
						| Assimp.aiProcess_LimitBoneWeights);

		// If scene could not be loaded, return null
		if (scene == null)
			return null;

		// Count scene information
		int meshCount = scene.mNumMeshes();

		ArrayList<UncompiledMesh> meshes = new ArrayList<>();

		// Load scene meshes
		for (int i = 0; i < meshCount; i++)
		{
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));

			UncompiledMesh meshData = AssimpMeshParser.loadMesh(mesh);
			AssimpSkeletonParser.loadSkeleton(scene, mesh, meshData);

			if (meshData != null)
				meshes.add(meshData);
		}

		// Cleanup and return
		scene.free();
		return meshes;
	}
}
