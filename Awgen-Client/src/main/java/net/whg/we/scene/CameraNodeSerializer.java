package net.whg.we.scene;

import java.nio.charset.StandardCharsets;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.scene.SceneNodeUtils;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class CameraNodeSerializer implements INodeSerializer
{
	@Override
	public String getNodeType()
	{
		return "player.camera";
	}

	@Override
	public void serialize(ByteWriter out, SceneNode node)
	{
		CameraNode n = (CameraNode) node;

		out.writeString(n.getName(), StandardCharsets.UTF_16);
		out.writeBool(n.isEnabled());
		SceneNodeUtils.writeTransform(out, n.getTransform());

		out.writeFloat(n.getFov());
		out.writeFloat(n.getNearClip());
		out.writeFloat(n.getFarClip());
	}

	@Override
	public SceneNode deserialize(ByteReader in)
	{
		CameraNode n = new CameraNode();

		n.setName(in.getString(StandardCharsets.UTF_16));
		n.setEnabled(in.getBool());
		n.setTransform(SceneNodeUtils.readTransform(in));

		n.setFov(in.getFloat());

		float near = in.getFloat();
		float far = in.getFloat();
		n.setClippingDistance(near, far);

		return n;
	}
}
