package net.whg.frameworks.scene;

/**
 * Represents a loaded scene with a given scene node hierarchy.
 *
 * @author TheDudeFromCI
 */
public class Scene
{
	private SceneNode _root;

	/**
	 * Creates a new scene with a default empty SceneNode as a root.
	 */
	public Scene()
	{
		_root = new SceneNode();
	}

	/**
	 * Gets the current root scene node for this scene.
	 *
	 * @return The current root scene node for this scene.
	 */
	public SceneNode getRoot()
	{
		return _root;
	}

	/**
	 * Gets the root scene node for this scene. If input is null, nothing happens.
	 *
	 * @param root
	 *            - The new scene node to assign as the root for this scene.
	 */
	public void setRoot(SceneNode root)
	{
		if (root == null)
			return;

		_root = root;
	}
}
