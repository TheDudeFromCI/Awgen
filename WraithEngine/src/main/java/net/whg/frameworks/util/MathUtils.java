package net.whg.frameworks.util;

import org.joml.Vector3f;

public class MathUtils
{
	/**
	 * This method checks if all the fields of a given vector are numbers.
	 *
	 * @param vec
	 *            a 3-dimensional vector
	 * @return true if the Vector is considered as Valid, false otherwise.
	 */
	public static boolean isValid(Vector3f vec)
	{
		if (Float.isNaN(vec.x))
			return false;
		if (Float.isNaN(vec.y))
			return false;
		if (Float.isNaN(vec.z))
			return false;
		return true;
	}

	/**
	 * This methods checks whether the given value x is between min and max. If x is
	 * smaller than min, it sets x at min If x is greater than max, it sets x at max
	 *
	 * @param x
	 *            the value to check
	 * @param min
	 *            the minimal accepted value
	 * @param max
	 *            the maximal accepted value
	 * @return the new value of x (or x)
	 */
	public static float clamp(float x, float min, float max)
	{
		if (x < min)
			return min;
		if (x > max)
			return max;
		return x;
	}
}
