package net.whg.we.scene;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public class SceneNode
{
	private String _name;
	private boolean _enabled;
	private Transform3D _transform;

	private SceneNode _parent;
	private List<SceneNode> _children = new ArrayList<>();

	private Matrix4f _matrixBuffer = new Matrix4f();

	public SceneNode()
	{
		_name = "Empty Node";
		_enabled = true;
		_transform = new Transform3D();
	}

	public String getName()
	{
		return _name;
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public void setName(String name)
	{
		if (name == null)
			name = "";

		_name = name;
	}

	public void setEnabled(boolean enabled)
	{
		_enabled = enabled;
	}

	public Transform3D getTransform()
	{
		return _transform;
	}

	public SceneNode getParent()
	{
		return _parent;
	}

	public int getChildCount()
	{
		return _children.size();
	}

	public SceneNode getChild(int index)
	{
		return _children.get(index);
	}

	private boolean isValidParent(SceneNode node)
	{
		SceneNode p = node;
		while (p != null)
		{
			if (p == this)
				return false;

			p = p.getParent();
		}

		return true;
	}

	public void setParent(SceneNode node)
	{
		if (_parent == node)
			return;

		// Check if we can set parent
		if (!isValidParent(node))
			throw new IllegalArgumentException("Circular heirarchy detected!");

		// Clear current parent if present
		if (_parent != null)
			_parent._children.remove(this);

		_parent = node;

		if (_parent != null)
			_parent._children.add(this);
	}

	public boolean isEnabledInHierarchy()
	{
		if (_parent == null)
			return _enabled;

		return _enabled && _parent.isEnabledInHierarchy();
	}

	public void getFullMatrix(Matrix4f out)
	{
		if (_parent == null)
		{
			_transform.getLocalMatrix(out);
			return;
		}

		_parent.getFullMatrix(_matrixBuffer);
		_transform.getFullMatrix(_matrixBuffer, out);
	}

	public void addChild(SceneNode node)
	{
		node.setParent(this);
	}
}
