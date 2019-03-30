package net.whg.we.client_logic.scene;

import net.whg.we.scene.Scene;
import net.whg.we.scene.SceneManager;

public class ClientSceneManger implements SceneManager
{
	private Scene _scene;

	public ClientSceneManger()
	{
		_scene = new Scene();
	}

	public Scene getScene()
	{
		return _scene;
	}

	public void setScene(Scene scene)
	{
		_scene = scene;
	}

	@Override
	public void updatePhysics()
	{
		_scene.update();
	}

	@Override
	public void updateFrame()
	{
		_scene.updateFrame();
	}

	@Override
	public void render()
	{
		_scene.render();
	}
}
