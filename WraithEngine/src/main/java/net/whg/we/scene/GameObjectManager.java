package net.whg.we.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.whg.we.main.GameState;
import net.whg.we.utils.Log;

/**
 * This class deals with handling game objects references.
 *
 * @author TheDudeFromCI
 */
public class GameObjectManager
{
	private List<GameObject> _activeGameObjects = new ArrayList<>();
	private List<GameObject> _toRemove = new LinkedList<>();
	private GameState _gameState;

	public GameObjectManager (GameState gameState)
	{
		_gameState = gameState;
	}

	/**
	 * Creates a new GameObject instance and adds it to the list of active objects.
	 */
	public GameObject createNew()
	{
		GameObject go = new GameObject(this);
		addGameObject(go);
		return go;
	}

	void addGameObject(GameObject gameObject)
	{
		_activeGameObjects.add(gameObject);
	}

	void removeGameObject(GameObject gameObject)
	{
		Log.tracef("Marked %s for disposal.", gameObject);
		_toRemove.add(gameObject);
	}

	/**
	 * Once called, this all objects currently marked for disposal will be removed from active game
	 * objects an disposed. This is intneded to be called at the end of a frame to allow to all
	 * game objects to finish their current tasks.
	 */
	public void endFrame()
	{
		for (GameObject go : _toRemove)
		{
			Log.tracef("Removed %s from active game objects.", go);

			_activeGameObjects.remove(go);
			go.dispose();
		}

		_toRemove.clear();
	}

	/**
	 * Gets the current GameState for this object.
	 */
	public GameState getGameState()
	{
		return _gameState;
	}
}
