package net.whg.we.resource;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import net.whg.frameworks.resource.ResourceFile;
import net.whg.we.client_logic.rendering.Skeleton;
import net.whg.we.client_logic.rendering.VertexData;

/**
 * A class containing all of the primative, raw data required to compiled a mesh
 * resource.
 *
 * @author TheDudeFromCI
 */
public class UncompiledMesh implements Externalizable
{
	private static final int FILE_VERSION = 1;

	/**
	 * The name of this mesh. Defaults to "untitled_mesh."
	 */
	public String name = "untitled_mesh";

	/**
	 * There vertex data for this mesh. Contains all vertices, triangles, and
	 * formating instructions for vertex attributes.
	 */
	public VertexData vertexData;

	/**
	 * The skeleton for this mesh, if this mesh is a skinned mesh. May be null.
	 */
	public Skeleton skeleton;

	/**
	 * Points to the resource file that this mesh data refers too. This value is
	 * transient and is only used during the saving and loading process. May be
	 * null or outdated.
	 */
	public transient ResourceFile path;

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(FILE_VERSION);
		out.writeObject(name);
		out.writeObject(vertexData);
		out.writeObject(skeleton);
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
				vertexData = (VertexData) in.readObject();
				skeleton = (Skeleton) in.readObject();
				return;

			default:
				throw new IllegalStateException(
						"Unknown file version: " + fileVersion + "!");
		}
	}
}
