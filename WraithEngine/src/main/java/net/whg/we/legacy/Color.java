package net.whg.we.legacy;

public class Color
{
	public float r, g, b, a;

	public Color()
	{
		r = 1f;
		g = 1f;
		b = 1f;
		a = 1f;
	}

	public Color(float lum)
	{
		r = lum;
		g = lum;
		b = lum;
		a = 1f;
	}

	public Color(float r, float g, float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		a = 1f;
	}

	public Color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public boolean isHDR()
	{
		return r > 1f || g > 1f || b > 1f;
	}

	public void setLumosity(float lum)
	{
		r = lum;
		g = lum;
		b = lum;
	}

	@Override
	public int hashCode()
	{
		return Float.floatToIntBits(r) ^ ~Float.floatToIntBits(g) ^ Float.floatToIntBits(b) ^ ~Float.floatToIntBits(a);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Color))
			return false;

		float e = 0.00001f;

		Color o = (Color) obj;
		return Math.abs(r - o.r) < e && Math.abs(g - o.g) < e && Math.abs(b - o.b) < e && Math.abs(a - o.a) < e;
	}

	@Override
	public String toString()
	{
		if (a < 1f)
			return String.format("Color: R:%.2f, G:%.2f, B:%.2f", r, g, b);

		return String.format("Color: R:%.2f, G:%.2f, B:%.2f, A:%.2f", r, g, b, a);
	}
}
