package net.whg.frameworks.external;

import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

public class AssimpSceneBridge implements AssimpScene
{
	private AssimpMesh[] _meshes;

	public AssimpSceneBridge(AIScene scene)
	{
		_meshes = new AssimpMesh[scene.mNumMeshes()];
		for (int i = 0; i < _meshes.length; i++)
			_meshes[i] = new AssimpMeshBridge(AIMesh.create(scene.mMeshes().get(i)));
	}

	@Override
	public int getMeshCount()
	{
		return _meshes.length;
	}

	@Override
	public AssimpMesh getMesh(int index)
	{
		return _meshes[index];
	}
}
