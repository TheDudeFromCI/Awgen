package net.whg.we.scene;

public class ServerSceneManager implements SceneManager
{
	private SceneList _sceneList;

	public ServerSceneManager()
	{
		_sceneList = new SceneList();
	}

	@Override
	public void updatePhysics()
	{
		for (int i = 0; i < _sceneList.getLoadedSceneCount(); i++)
			_sceneList.getLoadedScene(i).update();
	}

	@Override
	public void updateFrame()
	{
		for (int i = 0; i < _sceneList.getLoadedSceneCount(); i++)
			_sceneList.getLoadedScene(i).updateFrame();
	}

	@Override
	public void render()
	{
		for (int i = 0; i < _sceneList.getLoadedSceneCount(); i++)
			_sceneList.getLoadedScene(i).render();
	}
}
