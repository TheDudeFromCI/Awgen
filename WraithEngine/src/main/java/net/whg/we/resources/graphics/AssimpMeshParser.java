package net.whg.we.resources.graphics;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import net.whg.we.rendering.ShaderAttributes;
import net.whg.we.rendering.VertexData;
import net.whg.we.utils.logging.Log;

class AssimpMeshParser
{
	static VertexData loadMesh(AIMesh mesh)
	{
		// Count mesh information
		int boneCount = mesh.mNumBones();
		int vertexCount = mesh.mNumVertices();
		int triCount = mesh.mNumFaces();

		ShaderAttributes attributes = new ShaderAttributes();
		attributes.addAttribute("pos", 3);
		attributes.addAttribute("normal", 3);
		attributes.addAttribute("tangent", 3);
		attributes.addAttribute("bitangent", 3);

		if (mesh.mTextureCoords(0) != null)
		{
			attributes.addAttribute("uv", 2);

			int i = 1;
			while (mesh.mTextureCoords(i) != null)
			{
				attributes.addAttribute("uv" + (i + 1), 2);
				i++;
			}
		}

		if (boneCount > 0)
		{
			attributes.addAttribute("bone1", 4);
			attributes.addAttribute("bone2", 4);
		}

		if (Log.getLogLevel() <= Log.TRACE)
		{
			Log.indent();
			Log.trace("Mesh Data:");
			Log.indent();
			Log.tracef("Vertices: %s", vertexCount);
			Log.tracef("Triangles: %s", triCount);
			Log.tracef("Vertex Size: %s", attributes.getVertexSize());
			Log.tracef("Bones: %s", boneCount);
			Log.trace("Shader Attributes:");
			Log.indent();
			for (int i = 0; i < attributes.getCount(); i++)
				Log.tracef("%s: Size = %s", attributes.getAttributeName(i),
						attributes.getAttributeSize(i));
			Log.unindent();
			Log.unindent();
			Log.unindent();
		}

		// Build vertex data array
		int index = 0;
		float[] vertices = new float[vertexCount * attributes.getVertexSize()];
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

			AIVector3D tangent = mesh.mTangents().get(v);
			vertices[index++] = tangent.x();
			vertices[index++] = tangent.y();
			vertices[index++] = tangent.z();

			AIVector3D bitangent = mesh.mBitangents().get(v);
			vertices[index++] = bitangent.x();
			vertices[index++] = bitangent.y();
			vertices[index++] = bitangent.z();

			int texIndex = 0;
			while (mesh.mTextureCoords(texIndex) != null)
			{
				AIVector3D uv = mesh.mTextureCoords(texIndex).get(v);
				vertices[index++] = uv.x();
				vertices[index++] = uv.y();
				texIndex++;
			}

			// Add bone weight buffer, if needed
			if (boneCount > 0)
				index += 8;
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

		return new VertexData(vertices, triangles, attributes);
	}
}
