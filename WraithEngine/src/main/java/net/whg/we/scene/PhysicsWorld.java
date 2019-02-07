package net.whg.we.scene;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

public class PhysicsWorld
{
	private List<Collider> _colliders = new ArrayList<>();

	public void addCollider(Collider collider)
	{
		if (_colliders.contains(collider))
			return;

		_colliders.add(collider);
	}

	public void removeCollider(Collider collider)
	{
		_colliders.remove(collider);
	}

	public Collision raycast(Vector3f pos, Vector3f dir, float dist)
	{
		Collision col = null;

		for (Collider collider : _colliders)
		{
			Collision newCol = collider.collideRay(pos, dir, dist);
			if (newCol == null)
				continue;

			if (col == null || col.getDistance() > newCol.getDistance())
				col = newCol;
		}

		return col;
	}
}
