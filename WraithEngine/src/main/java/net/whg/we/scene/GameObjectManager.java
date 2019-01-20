package net.whg.we.scene;

import net.whg.we.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

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
		_toRemove.add(gameObject);
	}

	void endFrame()
	{
		for (GameObject go : _toRemove)
		{
			_activeGameObjects.remove(go);
			go.dispose();
		}

		_toRemove.clear();
	}
}
