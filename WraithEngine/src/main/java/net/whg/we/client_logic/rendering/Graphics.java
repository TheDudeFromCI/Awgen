package net.whg.we.client_logic.rendering;

import net.whg.we.legacy.Color;
import net.whg.we.resource.UncompiledTexture;

public interface Graphics
{
	void init();

	VMesh prepareMesh(VertexData vertexData);

	VTexture prepareTexture(UncompiledTexture data);

	VShader prepareShader(String vert, String geo, String frag);

	void clearScreenPass(ScreenClearType screenClear);

	void setClearScreenColor(Color color);

	void dispose();
}
