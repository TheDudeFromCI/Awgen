package whg.WraithEngine.rendering;

import org.joml.Matrix4f;

public class Skeleton
{
	public static final int MAX_BONES = 128;

	private Matrix4f _globalInverseTransform;
	private Bone[] _bones;
	private Bone _root;

	private Matrix4f _identityBuffer = new Matrix4f();

	public Skeleton(Matrix4f globalInverseTransform, Bone[] bones, Bone root)
	{
		_globalInverseTransform = globalInverseTransform;
		_bones = bones;
		_root = root;
	}

	public Matrix4f getGlobalInverseTransform()
	{
		return _globalInverseTransform;
	}

	public Bone[] getBones()
	{
		return _bones;
	}

	public Bone getRoot()
	{
		return _root;
	}

	public void updateHeirarchy()
	{
		updateHeirarchy(_root, _identityBuffer);
	}

	private void updateHeirarchy(Bone bone, Matrix4f transform)
	{
		bone.getGlobalTransform().set(transform);
		bone.getGlobalTransform().mul(bone.getLocalTransform());

		bone.getFinalTransform().set(_globalInverseTransform);
		bone.getFinalTransform().mul(bone.getGlobalTransform());
		bone.getFinalTransform().mul(bone.getOffsetMatrix());

		for (Bone b : bone.getChildren())
			updateHeirarchy(b, bone.getGlobalTransform());
	}

	public void setDefaultPose()
	{
		setDefaultPose(_root, _identityBuffer);
	}

	private void setDefaultPose(Bone bone, Matrix4f transform)
	{
		bone.getGlobalTransform().set(transform);
		bone.getGlobalTransform().mul(bone.getDefaultPose());

		bone.getFinalTransform().set(_globalInverseTransform);
		bone.getFinalTransform().mul(bone.getGlobalTransform());
		bone.getFinalTransform().mul(bone.getOffsetMatrix());

		for (Bone b : bone.getChildren())
			updateHeirarchy(b, bone.getGlobalTransform());
	}

}
