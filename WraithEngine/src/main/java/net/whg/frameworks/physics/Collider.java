package net.whg.frameworks.physics;

import org.joml.Vector3f;

public interface Collider
{
	public Collision collideRay(Vector3f pos, Vector3f dir, float dist);
}
