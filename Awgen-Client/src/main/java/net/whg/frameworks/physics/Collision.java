package net.whg.frameworks.physics;

import org.joml.Vector3f;

public class Collision
{
	private Vector3f _position;
	private Collider _collider;
	private Vector3f _normal;
	private float _distance;

	public Collision(Vector3f position, Collider collider, Vector3f normal, float distance)
	{
		_position = position;
		_collider = collider;
		_normal = normal;
		_distance = distance;
	}

	public Vector3f getPosition()
	{
		return _position;
	}

	public Collider getCollider()
	{
		return _collider;
	}

	public Vector3f getNormal()
	{
		return _normal;
	}

	public float getDistance()
	{
		return _distance;
	}
}
