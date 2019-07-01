package net.whg.frameworks.scene;

import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

/**
 * This interface is in charge of serializing a deserialing a set of scene
 * nodes.
 *
 * @author TheDudeFromCI
 */
public interface INodeFactory
{
	/**
	 * Loads a single node from the byte array. Only properties specific to the node
	 * need to be loaded and the hierarchy for this node (the parent or children)
	 * should be ignored.
	 *
	 * @param in
	 *            - The byte reader to read from.
	 * @return The very next, newly created scene node from the byte array.
	 * @throws UnsupportedNodeType
	 *             If this factory does not support the required node type.
	 */
	SceneNode readNode(ByteReader in);

	/**
	 * Saves a single node to the byte array. Only properties specific to the node
	 * need to be saved and the hierarchy for this node (the parent or children)
	 * should be ignored. The scene node may be any subclass of scene node.
	 *
	 * @param out
	 *            - The byte writer to write to.
	 * @param node
	 *            - The scene node to save.
	 * @throws UnsupportedNodeType
	 *             If this factory does not support the given node type.
	 */
	void writeNode(ByteWriter out, SceneNode node);
}
