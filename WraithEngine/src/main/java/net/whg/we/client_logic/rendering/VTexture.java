package net.whg.we.client_logic.rendering;

import net.whg.we.resource.TextureProperties;

public interface VTexture
{
	void bind(int textureSlot);

	void dispose();

	void recompile(TextureProperties properties);
}
