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
	SceneNode readNode(ByteReader in);

	void writeNode(ByteWriter out, SceneNode node);
}
