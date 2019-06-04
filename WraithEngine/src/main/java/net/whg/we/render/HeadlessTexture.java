package net.whg.we.render;

import net.whg.we.client_logic.rendering.VTexture;
import net.whg.we.resource.UncompiledTexture;

/**
 * Represents a headless texture implementation. This class has no internal
 * functionality.
 *
 * @author TheDudeFromCI
 */
public class HeadlessTexture implements VTexture
{
	@Override
	public void bind(int textureSlot)
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void recompile(UncompiledTexture data)
	{
	}
}
