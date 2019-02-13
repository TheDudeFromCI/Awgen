package net.whg.we.ui;

import net.whg.we.rendering.ShaderAttributes;
import net.whg.we.rendering.VertexData;

public class UIUtils
{
	public static VertexData defaultImageVertexData()
	{
		ShaderAttributes att = new ShaderAttributes();
		att.addAttribute("pos", 2);
		att.addAttribute("uv", 2);

		int v = 0;
		float[] vertices = new float[4 * att.getVertexSize()];

		// bottom left
		vertices[v++] = -0.5f;
		vertices[v++] = -0.5f;
		vertices[v++] = 0f;
		vertices[v++] = 1f;

		// bottom right
		vertices[v++] = 0.5f;
		vertices[v++] = -0.5f;
		vertices[v++] = 1f;
		vertices[v++] = 1f;

		// top left
		vertices[v++] = -0.5f;
		vertices[v++] = 0.5f;
		vertices[v++] = 0f;
		vertices[v++] = 0f;

		// top right
		vertices[v++] = 0.5f;
		vertices[v++] = 0.5f;
		vertices[v++] = 1f;
		vertices[v++] = 0f;

		int t = 0;
		short[] tris = new short[6];

		tris[t++] = 0;
		tris[t++] = 1;
		tris[t++] = 3;
		tris[t++] = 0;
		tris[t++] = 3;
		tris[t++] = 2;

		return new VertexData(vertices, tris, att);
	}
}
