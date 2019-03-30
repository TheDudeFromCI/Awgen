package net.whg.we.scene.behaviours;

import net.whg.we.client_logic.rendering.VertexData;
import net.whg.we.scene.GameObject;
import net.whg.we.scene.Location;
import net.whg.we.scene.ObjectBehaviour;

public class MeshColliderBehaviour implements ObjectBehaviour
{
	private GameObject _owner;
	// private MeshCollider _meshCollider;

	public MeshColliderBehaviour(VertexData vertexData, Location location)
	{
		// _meshCollider = new MeshCollider(vertexData, location);
	}

	@Override
	public void init(GameObject owner)
	{
		_owner = owner;
		// _owner.getManager().getScene().getPhysicsWorld()
		// .addCollider(_meshCollider);
	}

	@Override
	public GameObject getOwner()
	{
		return _owner;
	}

	@Override
	public void dispose()
	{
		// _owner.getManager().getScene().getPhysicsWorld()
		// .removeCollider(_meshCollider);

		_owner = null;
		// _meshCollider = null;
	}
}
