package net.whg.frameworks.scene;

public class Scene
{
	private SceneNode _root;

	public Scene()
	{
		_root = new SceneNode();
	}

	public SceneNode getRoot()
	{
		return _root;
	}

	public void setRoot(SceneNode root)
	{
		_root = root;
	}
}
