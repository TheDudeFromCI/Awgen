package net.whg.frameworks.external;

import java.nio.IntBuffer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;

public class AssimpMeshBridge implements AssimpMesh
{
	private String _name;
	private int _boneCount;
	private Vector3f[] _vertPositions;
	private Vector3f[] _vertNormals;
	private Vector2f[][] _vertUVs;
	private Vector3f[] _vertTangents;
	private Vector3f[] _vertBitangents;
	private Vector3i[] _triangles;

	public AssimpMeshBridge(AIMesh mesh)
	{
		_name = mesh.mName().dataString();
		_boneCount = mesh.mNumBones();

		// Count vertices
		int vertexCount = mesh.mNumVertices();
		int uvCount = 0;
		while (mesh.mTextureCoords(uvCount) != null)
			uvCount++;

		// Build buffers
		_vertPositions = new Vector3f[vertexCount];
		_vertNormals = new Vector3f[vertexCount];
		_vertUVs = new Vector2f[uvCount][vertexCount];

		if (mesh.mTangents() != null)
			_vertTangents = new Vector3f[vertexCount];

		if (mesh.mBitangents() != null)
			_vertBitangents = new Vector3f[vertexCount];

		// Fill buffers
		for (int i = 0; i < vertexCount; i++)
		{
			AIVector3D pos = mesh.mVertices().get(i);
			_vertPositions[i] = new Vector3f(pos.x(), pos.y(), pos.z());

			AIVector3D norm = mesh.mNormals().get(i);
			_vertNormals[i] = new Vector3f(norm.x(), norm.y(), norm.z());

			for (int j = 0; j < uvCount; j++)
			{
				AIVector3D uv = mesh.mTextureCoords(j).get(i);
				_vertUVs[j][i] = new Vector2f(uv.x(), uv.y());
			}

			if (_vertTangents != null)
			{
				AIVector3D tan = mesh.mTangents().get(i);
				_vertTangents[i] = new Vector3f(tan.x(), tan.y(), tan.z());
			}

			if (_vertBitangents != null)
			{
				AIVector3D bi = mesh.mBitangents().get(i);
				_vertBitangents[i] = new Vector3f(bi.x(), bi.y(), bi.z());
			}
		}

		// Count triangles
		int triangleCount = mesh.mNumFaces();
		_triangles = new Vector3i[triangleCount];

		// Fill triangles
		for (int i = 0; i < triangleCount; i++)
		{
			AIFace face = mesh.mFaces().get(i);
			IntBuffer in = face.mIndices();
			_triangles[i] = new Vector3i(in.get(0), in.get(1), in.get(2));
		}
	}

	@Override
	public String getName()
	{
		return _name;
	}

	@Override
	public int getBoneCount()
	{
		return _boneCount;
	}

	@Override
	public int getVertexCount()
	{
		return _vertPositions.length;
	}

	@Override
	public int getTriangleCount()
	{
		return _triangles.length;
	}

	@Override
	public boolean hasTangents()
	{
		return _vertTangents != null;
	}

	@Override
	public boolean hasBitangents()
	{
		return _vertBitangents != null;
	}

	@Override
	public int getUVCount()
	{
		return _vertUVs.length;
	}

	@Override
	public Vector3f getVertPosition(int index)
	{
		return _vertPositions[index];
	}

	@Override
	public Vector3f getVertNormal(int index)
	{
		return _vertNormals[index];
	}

	@Override
	public Vector2f getVertUV(int index, int uvLayer)
	{
		return _vertUVs[uvLayer][index];
	}

	@Override
	public Vector3f getVertTangent(int index)
	{
		return _vertTangents[index];
	}

	@Override
	public Vector3f getVertBitangent(int index)
	{
		return _vertBitangents[index];
	}

	@Override
	public Vector3i getTriangle(int index)
	{
		return _triangles[index];
	}
}
