package net.whg.frameworks.scene;

import java.util.ArrayList;
import java.util.List;
import net.whg.frameworks.util.GenericRunnable;

/**
 * Stores a list of scenes.
 *
 * @author TheDudeFromCI
 */
public class SceneList
{
	private List<Scene> _scenes = new ArrayList<>();

	/**
	 * Adds a new scene to this list.
	 *
	 * @param scene
	 *            - The scene to add to this list.
	 */
	public void addScene(Scene scene)
	{
		if (scene == null)
			return;

		if (_scenes.contains(scene))
			return;

		_scenes.add(scene);
	}

	/**
	 * Removes a scene from this list.
	 *
	 * @param scene
	 *            - The scene to remove from this list.
	 */
	public void removeScene(Scene scene)
	{
		if (scene == null)
			return;

		_scenes.remove(scene);
	}

	/**
	 * Gets the current number of scenes in this list.
	 *
	 * @return The number of scenes in this list.
	 */
	public int getSceneCount()
	{
		return _scenes.size();
	}

	/**
	 * Gets the scene at the specified index in this list.
	 *
	 * @param index
	 *            - The index of the scene in this list.
	 * @return The scene at the specified index.
	 */
	public Scene getSceneAt(int index)
	{
		return _scenes.get(index);
	}

	/**
	 * Gets the scene in this list with the specified scene id. If multiple scenes
	 * have the same id, the first scene found is returned.
	 *
	 * @param sceneId
	 *            - The id of the scene to return.
	 * @return The first scene in this list with the specified id, or null if there
	 *         is no scene in this list with that id.
	 */
	public Scene getSceneById(long sceneId)
	{
		for (Scene scene : _scenes)
			if (scene.getSceneId() == sceneId)
				return scene;
		return null;
	}

	/**
	 * Preforms an action on all scenes in this list.
	 *
	 * @param action
	 *            - The action to preform.
	 */
	public void forEach(GenericRunnable<Scene> action)
	{
		for (Scene scene : _scenes)
			action.run(scene);
	}

	/**
	 * Generates a new scene and adds it to this list. The scene is promised to have
	 * a scene id that does not exist in this list and is unquie to the newly
	 * generated scene.
	 * 
	 * @return The new scene that was generated.
	 */
	public Scene buildNewScene()
	{
		while (true)
		{
			Scene scene = new Scene();
			if (getSceneById(scene.getSceneId()) == null)
			{
				addScene(scene);
				return scene;
			}
		}
	}
}
