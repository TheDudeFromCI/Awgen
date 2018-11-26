package whg.WraithEngine.utils;

import java.io.File;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

import whg.WraithEngine.rendering.Bone;
import whg.WraithEngine.rendering.Mesh;
import whg.WraithEngine.rendering.ModelScene;
import whg.WraithEngine.rendering.Skeleton;
import whg.WraithEngine.rendering.SkinnedMesh;
import whg.WraithEngine.rendering.VertexData;

public class ModelLoader
{
	public static ModelScene loadModel(File file)
	{
		AIScene scene = Assimp.aiImportFile(file.toString(),
				Assimp.aiProcess_Triangulate |
				Assimp.aiProcess_GenSmoothNormals |
				Assimp.aiProcess_FlipUVs |
				Assimp.aiProcess_CalcTangentSpace |
				Assimp.aiProcess_LimitBoneWeights
			);
		
		ModelScene we_scene = new ModelScene();
		if (scene == null)
			return we_scene;
		
		for (int sceneId = 0; sceneId < scene.mNumMeshes(); sceneId++)
		{
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(sceneId));

			int boneCount = mesh.mNumBones();
			
			int sizeOfVertexUnrigged = 6;
			int sizeOfVertex = sizeOfVertexUnrigged + (boneCount > 0 ? 8 : 0);
			int vertexCount = mesh.mNumVertices();
			float[] vertices = new float[vertexCount * sizeOfVertex];

			int index = 0;
			for (int v = 0; v < vertexCount; v++)
			{
				AIVector3D pos = mesh.mVertices().get(v);
				vertices[index++] = pos.x();
				vertices[index++] = pos.y();
				vertices[index++] = pos.z();
				
				AIVector3D normal = mesh.mNormals().get(v);
				vertices[index++] = normal.x();
				vertices[index++] = normal.y();
				vertices[index++] = normal.z();
				
				if (boneCount > 0)
				{
					vertices[index++] = 0;
					vertices[index++] = 0;
					vertices[index++] = 0;
					vertices[index++] = 0;
					vertices[index++] = 0;
					vertices[index++] = 0;
					vertices[index++] = 0;
					vertices[index++] = 0;
				}
			}
			
			int triangleCount = mesh.mNumFaces();
			short[] triangles = new short[triangleCount * 3];

			// TODO If mesh has larger than 65k vertices, load as int array, not short array.
			// Or break into multiple meshes.
			index = 0;
			for (int f = 0; f < triangleCount; f++)
			{
				AIFace face = mesh.mFaces().get(f);
				triangles[index++] = (short)face.mIndices().get(0);
				triangles[index++] = (short)face.mIndices().get(1);
				triangles[index++] = (short)face.mIndices().get(2);
			}
			
			if (boneCount > 0)
			{
				HashMap<Integer,Integer> boneIndexMap = new HashMap<>();
				Bone[] bones = new Bone[mesh.mNumBones()];
				
				for (int b = 0; b < boneCount; b++)
				{
					AIBone bone = AIBone.create(mesh.mBones().get(b));

					String boneName = bone.mName().dataString();
					Matrix4f boneOffset = assimpMatrix(bone.mOffsetMatrix());
					Matrix4f bonePose = findDefaultPose(scene.mRootNode(), boneName);

					bones[b] = new Bone(boneName, boneOffset, bonePose, null);

					for (int w = 0; w < bone.mNumWeights(); w++)
					{
						AIVertexWeight weight = bone.mWeights().get(w);
						int vertexIndex = weight.mVertexId();
						int fIndex = vertexIndex * sizeOfVertex;
						
						if (!boneIndexMap.containsKey(vertexIndex))
						{
							vertices[(fIndex + sizeOfVertexUnrigged) + 0] = b;
							vertices[(fIndex + sizeOfVertexUnrigged) + 2] = weight.mWeight();
							boneIndexMap.put(vertexIndex, 1);
						}
						else if (boneIndexMap.get(vertexIndex) == 1)
						{
							vertices[(fIndex + sizeOfVertexUnrigged) + 1] = b;
							vertices[(fIndex + sizeOfVertexUnrigged) + 3] = weight.mWeight();
							boneIndexMap.put(vertexIndex, 2);
						}
						else if (boneIndexMap.get(vertexIndex) == 2)
						{
							vertices[(fIndex + sizeOfVertexUnrigged) + 4] = b;
							vertices[(fIndex + sizeOfVertexUnrigged) + 6] = weight.mWeight();
							boneIndexMap.put(vertexIndex, 3);
						}
						else if (boneIndexMap.get(vertexIndex) == 3)
						{
							vertices[(fIndex + sizeOfVertexUnrigged) + 5] = b;
							vertices[(fIndex + sizeOfVertexUnrigged) + 7] = weight.mWeight();
							boneIndexMap.put(vertexIndex, 4);
						}
						// TODO If a vertex has more than 4 bone parents, only use highest 4.
						// Currently it is ignored
					}
				}
				
				Bone rootBone = findRootBone(bones, scene.mRootNode());

				AIMatrix4x4 inverseRootTransformRaw = scene.mRootNode().mTransformation();
				Matrix4f inverseRootTransform = assimpMatrix(inverseRootTransformRaw);

				Skeleton skeleton = new Skeleton(inverseRootTransform, bones, rootBone);
				
				int[] attributes = new int[] {3, 3, 4, 4};
				VertexData vertexData = new VertexData(vertices, triangles, attributes);
				SkinnedMesh we_mesh = new SkinnedMesh(mesh.mName().dataString(), vertexData, skeleton);

				we_scene._meshes.add(we_mesh);
			}
			else
			{
				int[] attributes = new int[] {3, 3};
				VertexData vertexData = new VertexData(vertices, triangles, attributes);
				Mesh we_mesh = new Mesh(mesh.mName().dataString(), vertexData);

				we_scene._meshes.add(we_mesh);
			}
		}
		
		scene.free();
		
		return we_scene;
	}
	
	private static Bone findRootBone(Bone[] bones, AINode node)
	{
		Bone b = findBone(bones, node.mName().dataString());
		
		if (b != null)
		{
			buildBoneHeirarchy(bones, b, node);
			return b;
		}
		
		for (int i = 0; i < node.mNumChildren(); i++)
		{
			AINode n = AINode.create(node.mChildren().get(i));
			b = findRootBone(bones, n);
			
			if (b != null)
				return b;
		}
		
		return null;
	}
	
	private static void buildBoneHeirarchy(Bone[] bones, Bone bone, AINode node)
	{
		// TODO Add non-listed bones to bone hierarchy
		
		int childCount = node.mNumChildren();
		
		Bone[] children = new Bone[childCount];
		bone.setChildren(children);
		
		for (int i = 0; i < childCount; i++)
		{
			AINode n = AINode.create(node.mChildren().get(i));
			Bone b = findBone(bones, n.mName().dataString());
			
			if (b == null)
			{
				b = new Bone(n.mName().dataString(), assimpMatrix(n.mTransformation()).invert(),
						assimpMatrix(n.mTransformation()), null);
			}

			children[i] = b;
			buildBoneHeirarchy(bones, b, n);
		}
	}
	
	private static Bone findBone(Bone[] bones, String name)
	{
		for (int i = 0; i < bones.length; i++)
			if (bones[i].getBoneName().equals(name))
				return bones[i];
		return null;
	}
	
	private static Matrix4f findDefaultPose(AINode node, String boneName)
	{
		if (node.mName().dataString().equals(boneName))
			return assimpMatrix(node.mTransformation());
		
		for (int i = 0; i < node.mNumChildren(); i++)
		{
			AINode n = AINode.create(node.mChildren().get(i));
			Matrix4f mat = findDefaultPose(n, boneName);
			
			if (mat != null)
				return mat;
		}
		return null;
	}
	
	private static Matrix4f assimpMatrix(AIMatrix4x4 local)
	{
		Matrix4f m = new Matrix4f();
		m.m00(local.a1());
		m.m01(local.a2());
		m.m02(local.a3());
		m.m03(local.a4());

		m.m10(local.b1());
		m.m11(local.b2());
		m.m12(local.b3());
		m.m13(local.b4());

		m.m20(local.c1());
		m.m21(local.c2());
		m.m22(local.c3());
		m.m23(local.c4());

		m.m30(local.d1());
		m.m31(local.d2());
		m.m32(local.d3());
		m.m33(local.d4());
		
		m.transpose();

		return m;
	}
}
