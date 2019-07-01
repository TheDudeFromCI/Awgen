package net.whg.we.client_logic.rendering;

import net.whg.we.resource.UncompiledTexture;

public interface VTexture
{
	void bind(int textureSlot);

	void dispose();

	void recompile(UncompiledTexture data);
}
