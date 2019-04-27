package net.whg.we.resource;

import net.whg.frameworks.resource.ResourceFile;

/**
 * Represents a collection of data which can be used to constuct a texture
 * resource.
 *
 * @author TheDudeFromCI
 */
public class UncompiledTexture
{
	public String name;
	public TextureColorData colorData;
	public transient ResourceFile path;
}
