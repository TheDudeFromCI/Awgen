package net.whg.we.scene;

import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public interface INodeSerializer
{
	String getNodeType();

	void serialize(ByteWriter out, SceneNode node);

	SceneNode deserialize(ByteReader in);
}
