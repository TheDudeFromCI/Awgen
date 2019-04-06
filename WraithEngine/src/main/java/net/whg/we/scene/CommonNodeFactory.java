package net.whg.we.scene;

import java.util.ArrayList;
import java.util.List;
import net.whg.frameworks.scene.BaseNodeFactory;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class CommonNodeFactory extends BaseNodeFactory
{
	private List<INodeSerializer> _nodeSerializers = new ArrayList<>();

	public void addNodeSerializer(INodeSerializer serializer)
	{
		if (serializer == null)
			return;

		if (_nodeSerializers.contains(serializer))
			return;

		_nodeSerializers.add(serializer);
	}

	public void removeNodeSerializer(INodeSerializer serializer)
	{
		if (serializer == null)
			return;

		_nodeSerializers.remove(serializer);
	}

	@Override
	protected SceneNode loadNodeType(ByteReader in, String type)
	{
		for (INodeSerializer serializer : _nodeSerializers)
			if (serializer.getNodeType().equals(type))
				return serializer.deserialize(in);

		return super.loadNodeType(in, type);
	}

	@Override
	protected void saveNodeType(ByteWriter out, String type, SceneNode node)
	{
		for (INodeSerializer serializer : _nodeSerializers)
			if (serializer.getNodeType().equals(type))
			{
				serializer.serialize(out, node);
				return;
			}

		super.saveNodeType(out, type, node);
	}
}
