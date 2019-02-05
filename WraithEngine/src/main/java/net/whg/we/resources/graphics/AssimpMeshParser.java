package net.whg.we.resources.graphics;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import net.whg.we.rendering.VertexData;
import net.whg.we.utils.Log;

class AssimpMeshParser
{
	static final int VERT_POS_SIZE = 3;
	static final int VERT_NORMAL_SIZE = 3;
	static final int VERT_TEXTURE_SIZE = 2;
	static final int VERT_BONE_INDEX_SIZE = 4;
	static final int VERT_BONE_WEIGHT_SIZE = 4;

	static final int VERTEX_SIZE = VERT_POS_SIZE + VERT_NORMAL_SIZE + VERT_TEXTURE_SIZE;
	static final int VERTEX_SIZE_RIGGED =
			VERTEX_SIZE + VERT_BONE_INDEX_SIZE + VERT_BONE_WEIGHT_SIZE;

	static VertexData loadMesh(AIMesh mesh)
	{
		// Count mesh information
		int boneCount = mesh.mNumBones();
		int vertexCount = mesh.mNumVertices();
		int vertexSize = boneCount == 0 ? VERTEX_SIZE : VERTEX_SIZE_RIGGED;
		int triCount = mesh.mNumFaces();

		Log.indent();
		Log.trace("Mesh Data:");
		Log.indent();
		Log.tracef("Vertices: %s", vertexCount);
		Log.tracef("Triangles: %s", triCount);
		Log.tracef("Vertex Size: %s", vertexSize);
		Log.tracef("Bones: %s", boneCount);
		Log.unindent();
		Log.unindent();

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

			if (mesh.mTextureCoords(0) == null)
			{
				vertices[index++] = 0f;
				vertices[index++] = 0f;
			}
			else
			{
				AIVector3D uv = mesh.mTextureCoords(0).get(v);
				vertices[index++] = uv.x();
				vertices[index++] = uv.y();
			}

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

		// Assign attributes tag
		int[] attributes;
		if (boneCount > 0)
			attributes = new int[]
			{
					VERT_POS_SIZE, VERT_NORMAL_SIZE, VERT_TEXTURE_SIZE, VERT_BONE_INDEX_SIZE,
					VERT_BONE_WEIGHT_SIZE
			};
		else
			attributes = new int[]
			{
					VERT_POS_SIZE, VERT_NORMAL_SIZE, VERT_TEXTURE_SIZE
			};

		// Compile vertex data
		return new VertexData(vertices, triangles, attributes);
	}
}
