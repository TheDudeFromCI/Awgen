package net.whg.we.scene;

public class Scene
{
	private GameObjectManager _gameObjectManager;
	private RenderPass _renderPass;
	private LogicPass _logicPass;

	public Scene()
	{
		_gameObjectManager = new GameObjectManager(this);
		_renderPass = new RenderPass();
		_logicPass = new LogicPass();
	}

	public GameObjectManager getGameObjectManager()
	{
		return _gameObjectManager;
	}

	public RenderPass getRenderPass()
	{
		return _renderPass;
	}

	public LogicPass getLogicPass()
	{
		return _logicPass;
	}
}
