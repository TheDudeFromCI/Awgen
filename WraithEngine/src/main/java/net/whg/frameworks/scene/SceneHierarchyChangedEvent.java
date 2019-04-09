package net.whg.frameworks.scene;

/**
 * Represents an event called when a scene node attempts to change it's parent,
 * thus updating the scene hierarchy.
 *
 * @author TheDudeFromCI
 */
public class SceneHierarchyChangedEvent
{
	private Scene _scene;
	private SceneNode _node;
	private SceneNode _newParent;
	private boolean _isCanceled;

	/**
	 * Initializes a new event.
	 *
	 * @param scene
	 *            - The scene who's hierachy would be changed.
	 * @param node
	 *            - The node who attempting to assign a new parent.
	 * @param newParent
	 *            - The new parent that would be assigned to the node.
	 */
	public SceneHierarchyChangedEvent(Scene scene, SceneNode node, SceneNode newParent)
	{
		_scene = scene;
		_node = node;
		_newParent = newParent;
	}

	/**
	 * Gets the scene that would be effected.
	 *
	 * @return The scene for this event.
	 */
	public Scene getScene()
	{
		return _scene;
	}

	/**
	 * Gets the node who is attempted to assign a new parent. This node may or may
	 * not currently belong to the given scene.
	 *
	 * @return The node who's parent would be changing.
	 */
	public SceneNode getNode()
	{
		return _node;
	}

	/**
	 * Gets the new parent which would be assigned to this node. May be null if this
	 * node for this event is being removed from the scene.
	 *
	 * @return The new parent for the node.
	 */
	public SceneNode getNewParent()
	{
		return _newParent;
	}

	/**
	 * Sets whether this event should be canceled or not. If canceled, the parent
	 * for the node will not be changed. This value may be overriden by other
	 * listeners with a higher priority.
	 *
	 * @param canceled
	 *            - True if this event should be canceled, false otherwise.
	 */
	public void setCanceled(boolean canceled)
	{
		_isCanceled = canceled;
	}

	/**
	 * Check if this event is marked as canceled. If canceled, the parent for the
	 * node would not be updated.
	 *
	 * @return True if the event is currently marked as canceled. False otherwise.
	 */
	public boolean isCanceled()
	{
		return _isCanceled;
	}
}
