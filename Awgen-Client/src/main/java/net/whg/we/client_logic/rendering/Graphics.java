package net.whg.we.client_logic.rendering;

import net.whg.we.legacy.Color;
import net.whg.we.resource.UncompiledShader;
import net.whg.we.resource.UncompiledTexture;

public interface Graphics
{
	void init();

	VMesh prepareMesh(VertexData vertexData);

	VTexture prepareTexture(UncompiledTexture data);

	VShader prepareShader(UncompiledShader data);

	void clearScreenPass(ScreenClearType screenClear);

	void setClearScreenColor(Color color);

	void dispose();
}
