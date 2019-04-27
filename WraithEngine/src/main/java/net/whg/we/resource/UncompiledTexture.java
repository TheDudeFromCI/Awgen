package net.whg.we.resource;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.whg.frameworks.resource.ResourceFile;

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
	public String name;

	/**
	 * The color information and image dimensions of this texture.
	 */
	public TextureColorData colorData;

	/*
	 * A temporary pointer to the resource file, used while saving and loading
	 * texture data. This field is transient and may be null or outdata outside
	 * of saving or loading.
	 */
	public transient ResourceFile path;

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.write(FILE_VERSION);
		out.writeObject(name);
		out.writeObject(colorData);
	}

	@Override
	public void readExternal(ObjectInput in)
			throws IOException, ClassNotFoundException
	{
		int fileVersion = in.readInt();

		switch (fileVersion)
		{
			case 1:
				name = (String) in.readObject();
				colorData = (TextureColorData) in.readObject();
				return;

			default:
				throw new IllegalStateException(
						"Unknown file version: " + fileVersion + "!");
		}
	}
}
