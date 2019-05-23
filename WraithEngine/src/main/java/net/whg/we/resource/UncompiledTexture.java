package net.whg.we.resource;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.we.client_logic.rendering.NormalMapType;
import net.whg.we.client_logic.rendering.TextureSampleMode;

/**
 * Represents a collection of data which can be used to constuct a texture
 * resource.
 *
 * @author TheDudeFromCI
 */
public class UncompiledTexture implements Externalizable
{
	private static final int FILE_VERSION = 1;

	/**
	 * The name of this texture.
	 */
	public String name = "untitled_texture";

	/**
	 * The color information and image dimensions of this texture.
	 */
	public TextureColorData colorData;

	/*
	 * A temporary pointer to the resource file, used while saving and loading
	 * texture data. This field is transient and may be null or outdata outside of
	 * saving or loading.
	 */
	public transient ResourceFile path;

	/**
	 * Sets whether or not this texture should generate mipmaps when sent to the
	 * GPU.
	 */
	public boolean mipmapping = true;

	/**
	 * This determines how the texture should be sampled when it is rendered. The
	 * default setting is bilinear.
	 */
	public TextureSampleMode sampleMode = TextureSampleMode.BILINEAR;

	/**
	 * If this texture represnets a normal map, this determines which direction the
	 * normals are facing in the texture. If this texture is not a normal map, this
	 * value is assigned as non_normalmap.
	 */
	public NormalMapType normalMapType = NormalMapType.NON_NORMALMAP;

	/**
	 * Creates an empty, uncompiled texture object. This is given the default
	 * texture settings and is set as a 0x0 image texture with the IntRGBA color
	 * format.
	 */
	public UncompiledTexture()
	{
		colorData = new IntRGBAColorData();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.write(FILE_VERSION);
		out.writeObject(name);
		out.writeObject(colorData);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		int fileVersion = in.readInt();

		switch (fileVersion) {
			case 1:
				name = (String) in.readObject();
				colorData = (TextureColorData) in.readObject();
				return;

			default:
				throw new IllegalStateException("Unknown file version: " + fileVersion + "!");
		}
	}
}
