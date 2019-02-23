package net.whg.we.utils;

import org.joml.Vector3f;

public class MathUtils {
	static boolean isValid(Vector3f vec)
	{
		if (Float.isNaN(vec.x))
			return false;
		if (Float.isNaN(vec.y))
			return false;
		if (Float.isNaN(vec.z))
			return false;
		return true;
	}

	static float clamp(float x, float min, float max)
	{
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}
}

