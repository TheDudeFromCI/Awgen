package net.whg.we.resources;

import java.io.File;
import java.util.ArrayList;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

class AssimpSceneParser
{
	private AIScene _scene;
	private AssimpMeshParser _meshParser;
	private ArrayList<UncompiledMesh> _meshes;

	AssimpSceneParser(File file)
	{
		// Load the scene file
		_scene = Assimp.aiImportFile(file.toString(),
				Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals
						| Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace
						| Assimp.aiProcess_LimitBoneWeights);

		// If the scene cannot be loaded, cancel
		if (_scene == null)
			return;

		// Initialize the file parsers
		_meshParser = new AssimpMeshParser(_scene);
	}

	boolean isSceneLoaded()
	{
		return _scene != null;
	}

	void dispose()
	{
		_scene.free();
	}

	void load()
	{
		_meshes = new ArrayList<>();
		for (int i = 0; i < _meshParser.getMeshCount(); i++)
		{
			UncompiledMesh mesh = _meshParser.loadMesh(i);

			if (mesh == null)
				continue;

			_meshes.add(mesh);
		}
	}

	ArrayList<UncompiledMesh> getMeshes()
	{
		return _meshes;
	}
}
