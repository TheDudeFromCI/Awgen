package net.whg.we.rendering;

import org.joml.Matrix4f;

public class Bone
{
	private String _boneName;
	private Matrix4f _offset;
	private Matrix4f _defaultPose;
	private Bone[] _children;
	private Matrix4f _localTransform;
	private Matrix4f _globalTransform;
	private Matrix4f _finalTransform;

	public Bone(String boneName, Matrix4f offset, Matrix4f defaultPose,
			Bone[] children)
	{
		_boneName = boneName;
		_offset = offset;
		_defaultPose = defaultPose;
		_children = children;

		_localTransform = new Matrix4f();
		_globalTransform = new Matrix4f();
		_finalTransform = new Matrix4f();

		_localTransform.set(_defaultPose);
	}

	public Bone[] getChildren()
	{
		return _children;
	}

	public void setChildren(Bone[] children)
	{
		_children = children;
	}

	public String getBoneName()
	{
		return _boneName;
	}

	public Matrix4f getOffsetMatrix()
	{
		return _offset;
	}

	public Matrix4f getLocalTransform()
	{
		return _localTransform;
	}

	public Matrix4f getDefaultPose()
	{
		return _defaultPose;
	}

	public Matrix4f getFinalTransform()
	{
		return _finalTransform;
	}

	public Matrix4f getGlobalTransform()
	{
		return _globalTransform;
	}
}
