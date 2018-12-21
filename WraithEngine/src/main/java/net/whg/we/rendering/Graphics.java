package net.whg.we.rendering;

import net.whg.we.utils.Color;
import whg.WraithEngine.rendering.VertexData;

public interface Graphics
{
	public void init();

	public VMesh prepareMesh(VertexData vertexData);

	public void clearScreenPass(ScreenClearType screenClear);

	public void setClearScreenColor(Color color);

	public void dispose();
}
