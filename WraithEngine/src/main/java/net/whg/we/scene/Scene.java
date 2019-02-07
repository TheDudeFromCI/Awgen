package net.whg.we.scene;

public class Scene
{
	private GameObjectManager _gameObjectManager;
	private RenderPass _renderPass;
	private LogicPass _logicPass;
	private PhysicsWorld _physics;

	public Scene()
	{
		_gameObjectManager = new GameObjectManager(this);
		_renderPass = new RenderPass();
		_logicPass = new LogicPass();
		_physics = new PhysicsWorld();
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

	public PhysicsWorld getPhysicsWorld()
	{
		return _physics;
	}
}
