package net.whg.we.scene;

/**
 * This class represents the foundation for all objects that interact with the game directly. This
 * class is thread safe and is designed to directly interact with all core functions correctly. For
 * best practices, all entities should be derived from this class. This object is a wrapper for the
 * internal processes which may be disposed of as required by the GameObjectManager.
 *
 * @author TheDudeFromCI
 */
public class GameObject
{
	private GameObjectManager _manager;
	private boolean _disposed;
	private ArrayList<ObjectBehaviour> _behaviours = new ArrayList<>();

	GameObject(GameObjectManager manager)
	{
		_manager = manager;
		_raw = new GameObjectRaw();
	}

	/**
	 * Checks if this game object is currently disposed.
	 *
	 * @return True if the object is currently disposed. False otherwise.
	 */
	public boolean isDisposed()
	{
		return _raw == null;
	}

	/**
	 * Marks this game object for removal. This will tell it's owning GameObjectManager to mark this
	 * object for removal. It will be disposed at the end of the current frame.
	 */
	public void destroy()
	{
		_manager.removeGameObject(this);
	}

	/**
	 * Adds a new behaviour script to this GameObject. This will called the init method on the
	 * behaviour script, as well as marking that behaviour as belonging to this game object
	 * internally. An ObjectBehaviour should be created with the GameObject, and should only be
	 * attached to a single GameObject through it's life cycle.
	 *
	 * @param behaviour - The behaviour script to attach to this game object.
	 */
	public void addBehaviour(ObjectBehaviour behaviour)
	{
		if (_disposed)
		{
			Log.warn("Cannot add behaviour to already disposed object!");
			return;
		}

		if (!_behaviours.contains(behaviour))
		{
			_behaviours.add(behaviour);
			behaviour.init();
		}
	}

	/**
	 * Removes a behaviour script from this GameObject. This will called the dispose method on the
	 * behaviour script, as well as removing that behaviour from this game object internally. An
	 * ObjectBehaviour should be created with the GameObject, and should only be attached to a
	 * single GameObject through it's life cycle. After removing it from a GameObject, it can be
	 * reused again if desired.
	 *
	 * @param behaviour - The behaviour script to remove from this game object.
	 */
	public void removeBehaviour(ObjectBehaviour behaviour)
	{
		if (_disposed)
		{
			Log.warn("Cannot remove behaviour remove already disposed object!");
			return;
		}

		_behaviours.remove(behaviour);
		behaviour.dispose();
	}

	/**
	 * Gets the number of behavours currently attached to this game object.
	 *
	 * @return The number of behavours attached to this game object, or zero if this object is
	 * already disposed;
	 */
	public int getBehaviourCount()
	{
		if (_disposed)
			return 0;

		return _behaviours.size();
	}

	/**
	 * Gets the behaviour at this index.
	 *
	 * @param index - The index to get the behaviour at,
	 *
	 * @return THe object behaviour at the selected index, or null if this object is already
	 * disposed.
	 */
	public ObjectBehaviour getBehaviourAt(int index)
	{
		if (_disposed)
		{
			Log.warn("Cannot retrieve ObjectBehaviour from GameObject, already disposed!");
			return null;
		}

		return _behaviours.get(index);
	}

	void dispose()
	{
		if (_disposed)
			return;

		_disposed = true;
		_behaviours.clear();

		_manager = null;
		_behaviours = null;
	}
}
