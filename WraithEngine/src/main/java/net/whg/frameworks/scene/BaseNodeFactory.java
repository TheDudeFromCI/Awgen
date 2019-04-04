package net.whg.frameworks.scene;

import java.nio.charset.StandardCharsets;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

/**
 * A base implementation of the node factory. This value will correctly save and
 * parse scene hierarchies consisting of only empty scene nodes. This class also
 * implements a few helper methods for serializing and deserializing common
 * scene node data fields such as transforms.
 *
 * @author TheDudeFromCI
 */
public class BaseNodeFactory implements INodeFactory
{
	@Override
	public SceneNode readNode(ByteReader in)
	{
		String type = in.getString(StandardCharsets.UTF_8);
		return loadNodeType(in, type);
	}

	@Override
	public void writeNode(ByteWriter out, SceneNode node)
	{
		String nodeType = node.getNodeType();

		out.writeString(nodeType, StandardCharsets.UTF_8);
		saveNodeType(out, nodeType, node);
	}

	/**
	 * Called each time a node is attempted to be loaded with the given type. This
	 * method is used to determine how the next node should be parsed based on the
	 * given type.
	 *
	 * @param in
	 *            - The byte reader.
	 * @param type
	 *            - The type of the node to expect.
	 * @return The scene node which was loaded.
	 */
	protected SceneNode loadNodeType(ByteReader in, String type)
	{
		switch (type)
		{
			case "empty":
				return loadEmptySceneNode(in);

			default:
				throw new UnsupportedNodeType("Unknow node type! '" + type + "'");
		}
	}

	/***
	 * Called when a node needs to be saved.
	 *
	 * @param out
	 *            - The byte writer.
	 * @param type
	 *            - The type of the node to expect.
	 * @param node
	 *            - The node itself to serialize.
	 */
	protected void saveNodeType(ByteWriter out, String type, SceneNode node)
	{
		switch (type)
		{
			case "empty":
				saveEmptySceneNode(out, node);
				return;

			default:
				throw new UnsupportedNodeType("Unknow node type! '" + type + "'");
		}
	}

	/**
	 * Loads a simple empty scene node.
	 *
	 * @param in
	 *            - The byte reader to read from.
	 * @return The newly created scene node.
	 */
	protected SceneNode loadEmptySceneNode(ByteReader in)
	{
		SceneNode node = new SceneNode();
		node.setName(in.getString(StandardCharsets.UTF_16));
		node.setEnabled(in.getBool());
		node.setTransform(readTransform(in));

		return node;
	}

	/**
	 * Saves an empty scene node to the byte array.
	 *
	 * @param out
	 *            - The byte writer to write to.
	 * @param node
	 *            - The empty scene node to serialize.
	 */
	protected void saveEmptySceneNode(ByteWriter out, SceneNode node)
	{
		out.writeString(node.getName(), StandardCharsets.UTF_16);
		out.writeBool(node.isEnabled());
		writeTransform(out, node.getTransform());
	}

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
	protected void writeTransform(ByteWriter out, ITransform transform)
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
	protected ITransform readTransform(ByteReader in)
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
