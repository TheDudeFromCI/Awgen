package net.whg.we.scene;

import net.whg.frameworks.scene.SceneNode;
import net.whg.we.legacy.SceneList;
import net.whg.we.legacy.SceneManager;

public class SceneListManager implements SceneManager
{
	private SceneList _sceneList;

	public SceneListManager()
	{
		_sceneList = new SceneList();
	}

	@Override
	public void updatePhysics()
	{
		for (int i = 0; i < _sceneList.getLoadedSceneCount(); i++)
			updatePhysicsNodes(_sceneList.getLoadedScene(i).getRoot());
	}

	private void updatePhysicsNodes(SceneNode node)
	{
		if (node instanceof UpdateableNode)
			((UpdateableNode) node).update();

		for (int i = 0; i < node.getChildCount(); i++)
			updatePhysicsNodes(node.getChild(i));
	}

	@Override
	public void updateFrame()
	{
		for (int i = 0; i < _sceneList.getLoadedSceneCount(); i++)
			updateNodes(_sceneList.getLoadedScene(i).getRoot());
	}

	private void updateNodes(SceneNode node)
	{
		if (node instanceof UpdateableNode)
			((UpdateableNode) node).updateFrame();

		for (int i = 0; i < node.getChildCount(); i++)
			updateNodes(node.getChild(i));
	}

	@Override
	public void render()
	{
		for (int i = 0; i < _sceneList.getLoadedSceneCount(); i++)
			renderNodes(_sceneList.getLoadedScene(i).getRoot());
	}

	private void renderNodes(SceneNode node)
	{
		if (node instanceof RenderableNode)
			((RenderableNode) node).renderNode();

		for (int i = 0; i < node.getChildCount(); i++)
			renderNodes(node.getChild(i));
	}

	public SceneList getSceneList()
	{
		return _sceneList;
	}
}
