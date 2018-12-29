package net.whg.we.resources;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import net.whg.we.rendering.VertexData;

class AssimpMeshParser
{
	private static final int VERT_POS_SIZE = 3;
	private static final int VERT_NORMAL_SIZE = 3;
	private static final int VERT_BONE_INDEX_SIZE = 4;
	private static final int VERT_BONE_WEIGHT_SIZE = 4;

	private static final int VERTEX_SIZE = VERT_POS_SIZE + VERT_NORMAL_SIZE;
	private static final int VERTEX_SIZE_RIGGED =
			VERTEX_SIZE + VERT_BONE_INDEX_SIZE + VERT_BONE_WEIGHT_SIZE;

	private AIScene _scene;

	AssimpMeshParser(AIScene scene)
	{
		_scene = scene;
	}

	int getMeshCount()
	{
		return _scene.mNumMeshes();
	}

	UncompiledMesh loadMesh(int meshId)
	{
		// Get the referenced mesh
		AIMesh mesh = AIMesh.create(_scene.mMeshes().get(meshId));

		// Count mesh information
		int boneCount = mesh.mNumBones();
		int vertexCount = mesh.mNumVertices();
		int vertexSize = boneCount == 0 ? VERTEX_SIZE : VERTEX_SIZE_RIGGED;
		int triCount = mesh.mNumFaces();
		String meshName = mesh.mName().dataString();

		// Build vertex data array
		int index = 0;
		float[] vertices = new float[vertexCount * vertexSize];
		for (int v = 0; v < vertexCount; v++)
		{
			// Get position data
			AIVector3D pos = mesh.mVertices().get(v);
			vertices[index++] = pos.x();
			vertices[index++] = pos.y();
			vertices[index++] = pos.z();

			// Get normal data
			AIVector3D normal = mesh.mNormals().get(v);
			vertices[index++] = normal.x();
			vertices[index++] = normal.y();
			vertices[index++] = normal.z();

			// Add bone weight buffer, if needed
			if (boneCount > 0)
				index += VERT_BONE_INDEX_SIZE + VERT_BONE_WEIGHT_SIZE;
		}

		// Build triangle data array
		index = 0;
		short[] triangles = new short[triCount * 3];
		for (int f = 0; f < triCount; f++)
		{
			// Get vertex indices
			AIFace face = mesh.mFaces().get(f);
			triangles[index++] = (short) face.mIndices().get(0);
			triangles[index++] = (short) face.mIndices().get(1);
			triangles[index++] = (short) face.mIndices().get(2);
		}

		if (boneCount > 0)
		{
			// Assign attributes tag
			// int[] attributes = new int[]
			// {
			// VERT_POS_SIZE, VERT_NORMAL_SIZE, VERT_BONE_INDEX_SIZE, VERT_BONE_WEIGHT_SIZE
			// };

			// Load bone weights
			// TODO

			return null;
		}
		else
		{
			// Assign attributes tag
			int[] attributes = new int[]
			{
					VERT_POS_SIZE, VERT_NORMAL_SIZE
			};

			// Compile vertex data
			VertexData vertexData = new VertexData(vertices, triangles, attributes);
			return new UncompiledMesh(meshName, vertexData, null);
		}
	}
}
