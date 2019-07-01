package net.whg.we.resource;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import net.whg.frameworks.external.AssimpMesh;
import net.whg.frameworks.logging.Log;
import net.whg.we.client_logic.rendering.ShaderAttributes;
import net.whg.we.client_logic.rendering.VertexData;

class AssimpMeshParser
{
	static VertexData loadMesh(AssimpMesh mesh)
	{
		// Count mesh information
		int boneCount = mesh.getBoneCount();
		int vertexCount = mesh.getVertexCount();
		int triCount = mesh.getTriangleCount();

		ShaderAttributes attributes = new ShaderAttributes();
		attributes.addAttribute("pos", 3);
		attributes.addAttribute("normal", 3);

		if (mesh.hasTangents())
			attributes.addAttribute("tangent", 3);

		if (mesh.hasBitangents())
			attributes.addAttribute("bitangent", 3);

		if (mesh.getUVCount() > 0)
		{
			attributes.addAttribute("uv", 2);

			for (int i = 2; i <= mesh.getUVCount(); i++)
				attributes.addAttribute("uv" + i, 2);
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
				Log.tracef("%s: Size = %s", attributes.getAttributeName(i), attributes.getAttributeSize(i));
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
			Vector3f pos = mesh.getVertPosition(v);
			vertices[index++] = pos.x();
			vertices[index++] = pos.y();
			vertices[index++] = pos.z();

			// Get normal data
			Vector3f normal = mesh.getVertNormal(v);
			vertices[index++] = normal.x();
			vertices[index++] = normal.y();
			vertices[index++] = normal.z();

			if (mesh.hasTangents())
			{
				Vector3f tangent = mesh.getVertTangent(v);
				vertices[index++] = tangent.x();
				vertices[index++] = tangent.y();
				vertices[index++] = tangent.z();
			}

			if (mesh.hasBitangents())
			{
				Vector3f bitangent = mesh.getVertBitangent(v);
				vertices[index++] = bitangent.x();
				vertices[index++] = bitangent.y();
				vertices[index++] = bitangent.z();
			}

			for (int texIndex = 0; texIndex < mesh.getUVCount(); texIndex++)
			{
				Vector2f uv = mesh.getVertUV(v, texIndex);
				vertices[index++] = uv.x();
				vertices[index++] = uv.y();
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
			Vector3i face = mesh.getTriangle(f);
			triangles[index++] = (short) face.x();
			triangles[index++] = (short) face.y();
			triangles[index++] = (short) face.z();
		}

		return new VertexData(vertices, triangles, attributes);
	}
}
