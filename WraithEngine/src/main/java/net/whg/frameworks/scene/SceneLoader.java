package net.whg.frameworks.scene;

import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

/**
 * Saves and loads scene hierarchies from a byte array.
 *
 * @author TheDudeFromCI
 */
public class SceneLoader
{
	// TODO Very deep scenes (>6000 nodes) may throw a stack overflow.

	/**
	 * Loads a scene from a byte array using a node factory to parse nodes.
	 *
	 * @param in
	 *            - The byte array to read from.
	 * @param factory
	 *            - The factory in charge of deserializing scene nodes.
	 * @return The root scene node for this new scene hierarchy.
	 * @throws UnsupportedNodeType
	 *             If the given factory does not support a required node type.
	 */
	public static SceneNode loadScene(ByteReader in, INodeFactory factory)
	{
		SceneNode root = factory.readNode(in);
		loadChildren(root, in, factory);
		return root;
	}

	private static void loadChildren(SceneNode node, ByteReader in, INodeFactory factory)
	{
		int children = in.getInt();
		for (int i = 0; i < children; i++)
		{
			SceneNode child = factory.readNode(in);
			child.setParent(node);

			loadChildren(child, in, factory);
		}
	}

	/**
	 * Saves a scene to a byte array using a node factory to serialize each node.
	 *
	 * @param out
	 *            - The byte array to write to.
	 * @param factory
	 *            - The factory in charge os serializing each scene node.
	 * @param root
	 *            - The root node of the scene.
	 * @throws UnsupportedNodeType
	 *             If the given factory does not support a required node type.
	 */
	public static void saveScene(ByteWriter out, INodeFactory factory, SceneNode root)
	{
		factory.writeNode(out, root);
		saveChildren(root, out, factory);
	}

	private static void saveChildren(SceneNode node, ByteWriter out, INodeFactory factory)
	{
		out.writeInt(node.getChildCount());

		for (int i = 0; i < node.getChildCount(); i++)
		{
			SceneNode child = node.getChild(i);
			factory.writeNode(out, child);

			saveChildren(child, out, factory);
		}
	}
}
