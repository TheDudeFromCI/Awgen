package net.whg.we.scene;

/**
 * This class represents the foundation for all objects that interact with the
 * game directly. This class is thread safe and is designed to directly interact
 * with all core functions correctly. For best practices, all entities should be
 * derived from this class. This object is a wrapper for the internal processes
 * which may be disposed of as required by the GameObjectManager.
 *
 * @author TheDudeFromCI
 */
public class GameObject
{
	private GameObjectRaw _raw;

	public GameObject()
	{

	}

	public boolean isDisposed()
	{
		return _raw == null;
	}

	void dispose()
	{
		_raw.dispose();
		_raw = null;
	}
}
