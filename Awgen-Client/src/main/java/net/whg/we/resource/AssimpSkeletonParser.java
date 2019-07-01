package net.whg.we.resource;

import java.util.HashMap;
import org.joml.Matrix4f;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVertexWeight;
import net.whg.we.client_logic.rendering.Bone;
import net.whg.we.client_logic.rendering.Skeleton;
import net.whg.we.client_logic.rendering.VertexData;

class AssimpSkeletonParser
{
	static Skeleton loadSkeleton(AIScene scene, AIMesh mesh,
			VertexData vertexData)
	{
		int boneCount = mesh.mNumBones();

		if (boneCount == 0)
			return null;

		HashMap<Integer, Integer> boneIndexMap = new HashMap<>();
		Bone[] bones = new Bone[boneCount];
		float[] vertices = vertexData.getDataArray();
		int boneVertexPos =
				vertexData.getAttributeSizes().getPositionInVertex("bone1");

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
				int fIndex = vertexIndex * vertexData.getVertexSize();

				if (!boneIndexMap.containsKey(vertexIndex))
				{
					vertices[fIndex + boneVertexPos + 0] = b;
					vertices[fIndex + boneVertexPos + 2] = weight.mWeight();
					boneIndexMap.put(vertexIndex, 1);
				}
				else if (boneIndexMap.get(vertexIndex) == 1)
				{
					vertices[fIndex + boneVertexPos + 1] = b;
					vertices[fIndex + boneVertexPos + 3] = weight.mWeight();
					boneIndexMap.put(vertexIndex, 2);
				}
				else if (boneIndexMap.get(vertexIndex) == 2)
				{
					vertices[fIndex + boneVertexPos + 4] = b;
					vertices[fIndex + boneVertexPos + 6] = weight.mWeight();
					boneIndexMap.put(vertexIndex, 3);
				}
				else if (boneIndexMap.get(vertexIndex) == 3)
				{
					vertices[fIndex + boneVertexPos + 5] = b;
					vertices[fIndex + boneVertexPos + 7] = weight.mWeight();
					boneIndexMap.put(vertexIndex, 4);
				}
			}
		}

		Bone rootBone = findRootBone(bones, scene.mRootNode());

		AIMatrix4x4 inverseRootTransformRaw =
				scene.mRootNode().mTransformation();
		Matrix4f inverseRootTransform = assimpMatrix(inverseRootTransformRaw);

		return new Skeleton(inverseRootTransform, bones, rootBone);
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
				b = new Bone(n.mName().dataString(),
						assimpMatrix(n.mTransformation()).invert(),
						assimpMatrix(n.mTransformation()), null);
			}

			children[i] = b;
			buildBoneHeirarchy(bones, b, n);
		}
	}

	private static Bone findBone(Bone[] bones, String name)
	{
		for (Bone bone : bones)
			if (bone.getBoneName().equals(name))
				return bone;
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
