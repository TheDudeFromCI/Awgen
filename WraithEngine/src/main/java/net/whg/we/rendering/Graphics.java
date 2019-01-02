package net.whg.we.rendering;

import net.whg.we.utils.Color;

public interface Graphics
{
	public void init();

	public VMesh prepareMesh(VertexData vertexData);

	public VTexture prepareTexture(TextureProperties properties);

	public void clearScreenPass(ScreenClearType screenClear);

	public void setClearScreenColor(Color color);

	public void dispose();
}
