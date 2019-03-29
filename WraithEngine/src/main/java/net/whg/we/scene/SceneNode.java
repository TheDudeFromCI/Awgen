package net.whg.we.scene;

public class SceneNode
{
	private Transform _transform;
	private String _name;
	private boolean _enabled;
	private NodeType _type;

	public SceneNode(boolean is2D)
	{
		if (is2D)
			_transform = new Transform2D();
		else
			_transform = new Transform3D();

		_name = "Empty Node";
		_enabled = true;
		_type = NodeType.BLANK;
	}

	public Transform getTransform()
	{
		return _transform;
	}

	public String getName()
	{
		return _name;
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public NodeType getNodeType()
	{
		return _type;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setEnabled(boolean enabled)
	{
		_enabled = enabled;
	}

	public void setType(NodeType type)
	{
		_type = type;
	}
}
