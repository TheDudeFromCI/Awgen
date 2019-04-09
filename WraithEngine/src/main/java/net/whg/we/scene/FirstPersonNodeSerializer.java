package net.whg.we.scene;

import java.nio.charset.StandardCharsets;
import org.joml.Vector3f;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.scene.SceneNodeUtils;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class FirstPersonNodeSerializer implements INodeSerializer
{
	@Override
	public String getNodeType()
	{
		return "player.firstperson";
	}

	@Override
	public void serialize(ByteWriter out, SceneNode node)
	{
		FirstPersonNode n = (FirstPersonNode) node;

		out.writeString(n.getName(), StandardCharsets.UTF_16);
		out.writeBool(n.isEnabled());
		SceneNodeUtils.writeTransform(out, n.getTransform());

		Vector3f baseRot = n.getBaseRotation();
		out.writeFloat(baseRot.x);
		out.writeFloat(baseRot.y);
		out.writeFloat(baseRot.z);

		out.writeFloat(n.getMoveSpeed());
	}

	@Override
	public SceneNode deserialize(ByteReader in)
	{
		FirstPersonNode n = new FirstPersonNode();

		n.setName(in.getString(StandardCharsets.UTF_16));
		n.setEnabled(in.getBool());
		n.setTransform(SceneNodeUtils.readTransform(in));

		Vector3f baseRot = n.getBaseRotation();
		baseRot.x = in.getFloat();
		baseRot.y = in.getFloat();
		baseRot.z = in.getFloat();

		n.setMoveSpeed(in.getFloat());

		return n;
	}
}
