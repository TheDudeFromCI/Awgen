package net.whg.we.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import net.whg.we.rendering.VertexData;
import net.whg.we.utils.Location;

public class MeshCollider implements Collider
{
	private VertexData _vertexData;
	private Location _location;
	private Vector3f[] _posBuffer;
	private int _vertexPos;

	public MeshCollider(VertexData vertexData, Location location)
	{
		_vertexData = vertexData;
		_location = location;

		buildBuffers();
	}

	private void buildBuffers()
	{
		_vertexPos = _vertexData.getAttributeSizes().getPositionInVertex("pos");
		_posBuffer = new Vector3f[_vertexData.getVertexCount()];

		for (int i = 0; i < _posBuffer.length; i++)
			_posBuffer[i] = new Vector3f();
	}

	private void updateVertexPosBuffer()
	{
		Matrix4f mat = _location.getMatrix();
		float[] data = _vertexData.getDataArray();

		int j = 0;
		for (int i = _vertexPos; i < data.length; i += _vertexData.getVertexSize())
		{
			_posBuffer[j].x = data[i + 0];
			_posBuffer[j].y = data[i + 1];
			_posBuffer[j].z = data[i + 2];

			_posBuffer[j++].mulPosition(mat);
		}
	}

	@Override
	public Collision collideRay(Vector3f p, Vector3f d, float dist)
	{
		updateVertexPosBuffer();

		Vector3f hitPos = null;
		Vector3f hitNorm = null;
		float hitDist = Float.MAX_VALUE;

		// This just lets us reused vectors instead of allocating new ones per triangle
		Vector3f[] buffer = new Vector3f[5];
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = new Vector3f();

		short[] tris = _vertexData.getTriangles();
		for (int i = 0; i < tris.length; i += 3)
		{
			Vector3f v0 = _posBuffer[tris[i + 0] & 0xFFFF];
			Vector3f v1 = _posBuffer[tris[i + 1] & 0xFFFF];
			Vector3f v2 = _posBuffer[tris[i + 2] & 0xFFFF];

			Vector3f edge1 = v1.sub(v0, buffer[0]);
			Vector3f edge2 = v2.sub(v0, buffer[1]);

			Vector3f h = d.cross(edge2, buffer[2]);
			float a = edge1.dot(h);

			if (a > -0.0000001f && a < 0.0000001f)
				continue;

			float f = 1f / a;
			Vector3f s = p.sub(v0, buffer[3]);
			float u = f * s.dot(h);
			if (u < 0f || u > 1f)
				continue;

			Vector3f q = s.cross(edge1, buffer[4]);
			float v = f * d.dot(q);

			if (v < 0f || u + v > 1f)
				continue;

			float t = f * edge2.dot(q);
			if (t <= 0.0000001f)
				continue;

			// We have a hit!
			// Is it closer than our last hit?
			if (t >= hitDist)
				continue;

			hitPos = d.mul(t, new Vector3f()).add(p);
			hitNorm = edge1.cross(edge2, new Vector3f()).normalize();
			hitDist = t;
		}

		if (hitPos == null)
			return null;

		return new Collision(hitPos, this, hitNorm, hitDist);
	}
}
