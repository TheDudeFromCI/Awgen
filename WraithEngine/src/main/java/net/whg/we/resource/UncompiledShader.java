package net.whg.we.resource;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.whg.frameworks.resource.ResourceFile;

/**
 * Represents a collection of data which can be used to construct a shader
 * resource.
 *
 * @author TheDudeFromCI
 */
public class UncompiledShader implements Externalizable
{
	private static final int FILE_VERSION = 1;

	/**
	 * The nameof this shader.
	 */
	public String name;

	/**
	 * The vertex program for this shader.
	 */
	public String vertShader;

	/**
	 * The geometry program for this shader. May by null in cases where the shader
	 * does not use a geometry shader.
	 */
	public String geoShader;

	/**
	 * The fragment program for this shader.
	 */
	public String fragShader;

	/*
	 * A temporary pointer to the resource file, used while saving and loading
	 * shader data. This field is transient and may be null or outdata outside of
	 * saving or loading.
	 */
	public transient ResourceFile path;

	@Override
	public int hashCode()
	{
		int hash = 0;

		if (name != null)
			hash ^= name.hashCode();

		if (vertShader != null)
			hash ^= ~vertShader.hashCode();

		if (geoShader != null)
			hash ^= geoShader.hashCode();

		if (fragShader != null)
			hash ^= ~fragShader.hashCode();

		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof UncompiledShader))
			return false;

		UncompiledShader o = (UncompiledShader) obj;
		return safeEquals(name, o.name) && safeEquals(vertShader, o.vertShader) && safeEquals(geoShader, o.geoShader)
				&& safeEquals(fragShader, o.fragShader);
	}

	private boolean safeEquals(Object a, Object b)
	{
		if (a == null != (b == null))
			return false;
		if (a == null)
			return true;

		return a.equals(b);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(FILE_VERSION);
		out.writeObject(name);
		out.writeObject(vertShader);
		out.writeObject(geoShader);
		out.writeObject(fragShader);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		int fileVersion = in.readInt();

		switch (fileVersion) {
			case 1:
				name = (String) in.readObject();
				vertShader = (String) in.readObject();
				geoShader = (String) in.readObject();
				fragShader = (String) in.readObject();
				return;

			default:
				throw new IllegalStateException("Unknown file version: " + fileVersion + "!");
		}
	}
}
