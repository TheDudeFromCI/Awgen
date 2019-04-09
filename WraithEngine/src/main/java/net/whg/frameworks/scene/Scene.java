package net.whg.frameworks.scene;

import java.util.Random;

/**
 * Represents a loaded scene with a given scene node hierarchy and a unquie
 * scene id.
 *
 * @author TheDudeFromCI
 */
public class Scene
{
	private static long randomId()
	{
		/*
		 * Because java.util.Random only uses 48 bits of precision, there's only 2^48
		 * posible outputs. Which is still fairly random, but I feel safer with a full
		 * 2^64, tbh.
		 */
		// return new Random().nextLong()

		// Here I build two RNGs to ensure that the number is sufficently random.
		int a = new Random().nextInt();
		int b = new Random().nextInt();
		return (a << 32L) + b;
	}

	private long _sceneId;
	private SceneNode _root;
	private SceneEvent _event;

	/**
	 * Creates a new scene with a default empty scene node as a root. This scene is
	 * given a randomized scene id.
	 */
	public Scene()
	{
		this(randomId());
	}

	/**
	 * Creates a new scene with a default empty scene node as a root, with the given
	 * scene id.
	 *
	 * @param sceneId
	 *            - The scene id to assign to this scene.
	 */
	public Scene(long sceneId)
	{
		_sceneId = sceneId;
		setRoot(new SceneNode());
		_event = new SceneEvent();
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

		if (_root != null)
			_root.setScene(null);

		_root = root;
		_root.setScene(this);
	}

	/**
	 * Gets the current scene id for this scene.
	 *
	 * @return The scene id for this scene.
	 */
	public long getSceneId()
	{
		return _sceneId;
	}

	/**
	 * Gets the event caller for this scene.
	 * 
	 * @return The event caller for this scene.
	 */
	public SceneEvent getEvent()
	{
		return _event;
	}
}
