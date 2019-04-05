package net.whg.frameworks.scene;

public class SceneHierarchyChangedEvent
{
	private Scene _scene;
	private SceneNode _node;
	private SceneNode _newParent;
	private boolean _isCanceled;

	public SceneHierarchyChangedEvent(Scene scene, SceneNode node, SceneNode oldParent, SceneNode newParent)
	{
		_scene = scene;
		_node = node;
		_newParent = newParent;
	}

	public Scene getScene()
	{
		return _scene;
	}

	public SceneNode getNode()
	{
		return _node;
	}

	public SceneNode getNewParent()
	{
		return _newParent;
	}

	public void setCanceled(boolean canceled)
	{
		_isCanceled = canceled;
	}

	public boolean isCanceled()
	{
		return _isCanceled;
	}
}
