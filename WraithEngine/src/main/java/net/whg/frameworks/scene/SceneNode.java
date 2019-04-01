package net.whg.frameworks.scene;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

/**
 * Represents a single node in a scene hierarchy tree.
 *
 * @author TheDudeFromCI
 */
public class SceneNode
{
	// Fields
	private String _name;
	private boolean _enabled;
	private ITransform _transform;

	private SceneNode _parent;
	private List<SceneNode> _children = new ArrayList<>();

	// Temp
	private Matrix4f _matrixBuffer = new Matrix4f();

	/**
	 * Creates a new blank scene node with default settings.
	 */
	public SceneNode()
	{
		_name = "Empty Node";
		_enabled = true;
		_transform = new Transform3D();
	}

	/**
	 * Gets the current name of this scene node.
	 *
	 * @return The name of this scene node.
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * Checks if this scene node is currently locally enabled.
	 *
	 * @return True if this scene node is marked as enabled, false otherwise.
	 */
	public boolean isEnabled()
	{
		return _enabled;
	}

	/**
	 * Sets a new name for this scene node.
	 *
	 * @param name
	 *            - The new name for this scene node.
	 */
	public void setName(String name)
	{
		if (name == null)
			name = "";

		_name = name;
	}

	/**
	 * Sets whether this scene node is enabled or not.
	 *
	 * @param enabled
	 *            - True if this scene node should be enabled, false if not.
	 */
	public void setEnabled(boolean enabled)
	{
		_enabled = enabled;
	}

	/**
	 * Gets the current transform of this scene node.
	 *
	 * @return The transform of this scene node.
	 */
	public ITransform getTransform()
	{
		return _transform;
	}

	/**
	 * Sets a new transform object for this scene node.
	 *
	 * @param transform
	 *            - The new scene transform object.
	 */
	public void setTransform(ITransform transform)
	{
		_transform = transform;
	}

	/**
	 * Gets the current parent of this scene node.
	 *
	 * @return The parent of this scene node, or null if no parent is assigned.
	 */
	public SceneNode getParent()
	{
		return _parent;
	}

	/**
	 * Gets the current number of children for this scene node.
	 *
	 * @return The number of children this scene node has.
	 */
	public int getChildCount()
	{
		return _children.size();
	}

	/**
	 * Gets a child scene node based on it's index.
	 *
	 * @param index
	 *            - The index of the child to return.
	 * @return The scene node at the given child index.
	 */
	public SceneNode getChild(int index)
	{
		return _children.get(index);
	}

	private boolean isValidParent(SceneNode node)
	{
		SceneNode p = node;
		while (p != null)
		{
			if (p == this)
				return false;

			p = p.getParent();
		}

		return true;
	}

	/**
	 * Assigns a new parent to this scene node. If this scene node already has a
	 * parent, this node is removed from the previous parent's children list and
	 * this object is added to the list of the new parent. If the input is null, any
	 * currently assigned parents are removed.
	 *
	 * @param node
	 *            - The scene node to assign as the parent of this scene node.
	 * @throws IllegalArgumentException
	 *             If circular parent dependency is detected.
	 */
	public void setParent(SceneNode node)
	{
		if (_parent == node)
			return;

		// Check if we can set parent
		if (!isValidParent(node))
			throw new IllegalArgumentException("Circular heirarchy detected!");

		// Clear current parent if present
		if (_parent != null)
			_parent._children.remove(this);

		_parent = node;

		if (_parent != null)
			_parent._children.add(this);
	}

	/**
	 * Checks if this scene node is enabled, and all of it's parents are currently
	 * enabled.
	 *
	 * @return True if this node and all of its ancestors are currently enabled.
	 *         False otherwise.
	 */
	public boolean isEnabledInHierarchy()
	{
		if (_parent == null)
			return _enabled;

		return _enabled && _parent.isEnabledInHierarchy();
	}

	/**
	 * A short hand method for calculating the local transform matrix of this scene
	 * node.
	 *
	 * @param out
	 *            - The matrix to store the output into.
	 */
	public void getLocalMatrix(Matrix4f out)
	{
		_transform.getLocalMatrix(out);
	}

	/**
	 * A short hand method for calculating the full transform matrix of this scene
	 * node based on the transforms of all the given parents.
	 *
	 * @param out
	 *            - The matrix to store the output into.
	 */
	public void getFullMatrix(Matrix4f out)
	{
		if (_parent == null)
		{
			_transform.getLocalMatrix(out);
			return;
		}

		_parent.getFullMatrix(_matrixBuffer);
		_transform.getFullMatrix(_matrixBuffer, out);
	}

	/**
	 * Add a child to this scene node. This method is identical to calling
	 * {@link #setParent(SceneNode)} on the input node.
	 *
	 * @param node
	 *            - The node to add as a child to this node.
	 */
	public void addChild(SceneNode node)
	{
		node.setParent(this);
	}
}
