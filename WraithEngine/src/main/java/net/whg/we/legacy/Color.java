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
}
