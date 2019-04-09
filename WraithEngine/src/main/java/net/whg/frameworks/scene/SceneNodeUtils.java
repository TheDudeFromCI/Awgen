package net.whg.frameworks.scene;

import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class SceneNodeUtils
{
	/**
	 * Attempts to write a transform to the byte writer output. This method not not
	 * correctly handle all given transforms, and currently only supports the
	 * following transform subclasses:
	 * <ul>
	 * <li>{@link net.whg.frameworks.scene.Transform3D}</li>
	 * </ul>
	 *
	 * @param out
	 *            - The byte writer to write to.
	 * @param transform
	 *            - The transform to attempt to serialize.
	 * @throws IllegalStateException
	 *             If the transform is not currently a supported type.
	 */
	public static void writeTransform(ByteWriter out, ITransform transform)
	{
		// Transform IDs:
		// 1 = Transform3D

		if (transform instanceof Transform3D)
		{
			out.writeByte(1); // id

			Transform3D t = (Transform3D) transform;
			out.writeFloat(t.getPosition().x);
			out.writeFloat(t.getPosition().y);
			out.writeFloat(t.getPosition().z);
			out.writeFloat(t.getRotation().x);
			out.writeFloat(t.getRotation().y);
			out.writeFloat(t.getRotation().z);
			out.writeFloat(t.getRotation().w);
			out.writeFloat(t.getSize().x);
			out.writeFloat(t.getSize().y);
			out.writeFloat(t.getSize().z);
			return;
		}

		throw new IllegalStateException("Unsupported transform type!");
	}

	/**
	 * Attempts to read a transform from the byte array. This method only works if
	 * the transform was written with the corrosponding
	 * {@link #writeTransform(ByteWriter, ITransform)} function in this class.
	 *
	 * @param in
	 *            - The byte reader to reader from
	 * @return A newly created transform, loaded from the byte array.
	 */
	public static ITransform readTransform(ByteReader in)
	{
		int type = in.getByte();

		if (type == 1)
		{
			Transform3D t = new Transform3D();
			t.getPosition().x = in.getFloat();
			t.getPosition().y = in.getFloat();
			t.getPosition().z = in.getFloat();
			t.getRotation().x = in.getFloat();
			t.getRotation().y = in.getFloat();
			t.getRotation().z = in.getFloat();
			t.getRotation().w = in.getFloat();
			t.getSize().x = in.getFloat();
			t.getSize().y = in.getFloat();
			t.getSize().z = in.getFloat();
			return t;
		}

		throw new IllegalStateException("Unsupported transform type!");
	}
}
