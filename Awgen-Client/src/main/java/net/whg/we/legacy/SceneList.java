package net.whg.we.legacy;

import java.util.ArrayList;
import java.util.List;
import net.whg.frameworks.scene.Scene;

public class SceneList
{
	private List<Scene> _loadedScenes = new ArrayList<>();

	public SceneList()
	{
		addScene(new Scene());
	}

	public int getLoadedSceneCount()
	{
		return _loadedScenes.size();
	}

	public Scene getLoadedScene(int index)
	{
		return _loadedScenes.get(index);
	}

	public void addScene(Scene scene)
	{
		if (scene == null)
			return;

		if (_loadedScenes.contains(scene))
			return;

		_loadedScenes.add(scene);
	}

	public void removeScene(Scene scene)
	{
		if (scene == null)
			return;

		_loadedScenes.remove(scene);
	}
}
