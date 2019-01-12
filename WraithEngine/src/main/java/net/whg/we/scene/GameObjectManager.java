package net.whg.we.scene;

import java.util.ArrayList;
import net.whg.we.utils.Log;

/**
 * This class deals with handling all game object resource handling.
 *
 * @author TheDudeFromCI
 */
public class GameObjectManager
{
	private static ArrayList<GameObject> _activeGameObjects = new ArrayList<>();

	static void addGameObject(GameObject gameObject)
	{
		if (_activeGameObjects.contains(gameObject))
		{
			Log.warn("Tried to register a game object that already exists!");
			return;
		}

		_activeGameObjects.add(gameObject);
	}

	static void removeGameObject(GameObject gameObject)
	{
		if (Log.getLogLevel() <= Log.DEBUG)
		{
			if (!_activeGameObjects.contains(gameObject))
			{
				Log.warn("Tried to unregister a game object that does not exist!");
				return;
			}

			_activeGameObjects.remove(gameObject);
			gameObject.dispose();
		}
	}

	static void endFrame()
	{
	}
}
