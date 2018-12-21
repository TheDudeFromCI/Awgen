package net.whg.we.rendering;

import whg.WraithEngine.rendering.VertexData;

public interface Graphics
{
	public void init();

	public VMesh prepareMesh(VertexData vertexData);

	public void clearScreenPass(ScreenClearType screenClear);
}
