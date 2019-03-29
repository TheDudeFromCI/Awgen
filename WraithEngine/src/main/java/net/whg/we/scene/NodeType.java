package net.whg.we.scene;

public enum NodeType
{
	BLANK("Blank"),
	MESH("Mesh"),
	BEHAVIOUR("Behaviour"),
	COLLIDER("Collider"),
	PARTICLE_EFFECT("Particle Effect"),
	OBJECT_LINK("Object Link"),
	BONE("Bone");

	private String _name;

	private NodeType(String name)
	{
		_name = name;
	}

	public String getName()
	{
		return _name;
	}
}
