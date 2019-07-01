package net.whg.awgen.lib.util;

import org.joml.Math;

public class MathLib
{
	/**
	 * Clamps the given values within the defined range.
	 *
	 * @param x
	 *     - The value.
	 * @param min
	 *     - The minimum value.
	 * @param max
	 *     - The maximum value.
	 * @return The new value.
	 */
	public static float clamp(float x, float min, float max)
	{
		if (x < min)
			return min;

		if (x > max)
			return max;

		return x;
	}

	/**
	 * A shorthand methopd for clamping the x value within the 0 to 1 range.
	 *
	 * @param x
	 *     - The value.
	 * @return The new value.
	 */
	public static float clamp01(float x)
	{
		return clamp(x, 0, 1);
	}

	/**
	 * Preforms a standard floor operation on the variable, returning the result in
	 * the form of an int.
	 *
	 * @param x
	 *     - The value.
	 * @return The new value.
	 */
	public static int floorToInt(double x)
	{
		return (int) Math.floor(x);
	}
}
