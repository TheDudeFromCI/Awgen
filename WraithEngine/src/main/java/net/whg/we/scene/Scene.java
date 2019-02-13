package net.whg.we.scene;

import org.lwjgl.opengl.GL11;
import net.whg.we.ui.UIStack;

public class Scene
{
	private GameObjectManager _gameObjectManager;
	private LogicPass _logicPass;
	private RenderPass _renderPass;
	private UIStack _uiStack;
	private PhysicsWorld _physics;

	public Scene()
	{
		_gameObjectManager = new GameObjectManager(this);
		_logicPass = new LogicPass();
		_renderPass = new RenderPass();
		_uiStack = new UIStack();
		_physics = new PhysicsWorld();
	}

	public GameObjectManager getGameObjectManager()
	{
		return _gameObjectManager;
	}

	public PhysicsWorld getPhysicsWorld()
	{
		return _physics;
	}

	public LogicPass getLogicPass()
	{
		return _logicPass;
	}

	public RenderPass getRenderPass()
	{
		return _renderPass;
	}

	public UIStack getUIStack()
	{
		return _uiStack;
	}

	public void update()
	{
		_logicPass.updatePass();
		_uiStack.update();
	}

	public void updateFrame()
	{
		_logicPass.updateFramePass();
		_uiStack.updateFrame();
	}

	public void render()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		_renderPass.render();

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		_uiStack.render();
	}
}
