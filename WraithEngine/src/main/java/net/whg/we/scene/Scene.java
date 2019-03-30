package net.whg.we.scene;

import org.lwjgl.opengl.GL11;
import net.whg.we.client_logic.ui.UIStack;

public class Scene
{
	private SceneNode _sceneNode;
	private UIStack _uiStack;

	public Scene()
	{
		_uiStack = new UIStack();

		_sceneNode = new SceneNode();
	}

	public SceneNode getSceneNode()
	{
		return _sceneNode;
	}

	public void setSceneNode(SceneNode sceneNode)
	{
		_sceneNode = sceneNode;
	}

	public UIStack getUIStack()
	{
		return _uiStack;
	}

	public void update()
	{
		_uiStack.update();
	}

	public void updateFrame()
	{
		_uiStack.updateFrame();
	}

	private void renderScene(SceneNode node)
	{
		if (node instanceof RenderableNode)
			((RenderableNode) node).renderNode();

		for (int i = 0; i < node.getChildCount(); i++)
			renderScene(node.getChild(i));
	}

	public void render()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		renderScene(_sceneNode);

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		_uiStack.render();
	}

	public void dispose()
	{
		_uiStack.dispose();
	}
}
