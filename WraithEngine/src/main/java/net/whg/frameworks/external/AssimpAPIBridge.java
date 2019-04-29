package net.whg.frameworks.external;

import java.io.File;
import org.lwjgl.assimp.AIPropertyStore;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

public class AssimpAPIBridge implements AssimpAPI
{
	@Override
	public AssimpScene load(File file)
	{
		AIPropertyStore settings = Assimp.aiCreatePropertyStore();
		Assimp.aiSetImportPropertyInteger(settings, Assimp.AI_CONFIG_PP_SLM_VERTEX_LIMIT, 65535);

		AIScene scene = Assimp.aiImportFile(file.toString(),
				Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_FlipUVs
						| Assimp.aiProcess_CalcTangentSpace | Assimp.aiProcess_LimitBoneWeights
						| Assimp.aiProcess_SplitLargeMeshes | Assimp.aiProcess_OptimizeMeshes
						| Assimp.aiProcess_JoinIdenticalVertices);

		Assimp.aiReleasePropertyStore(settings);

		if (scene == null)
			return null;

		AssimpScene s = new AssimpSceneBridge(scene);
		Assimp.aiReleaseImport(scene);

		return s;
	}
}
