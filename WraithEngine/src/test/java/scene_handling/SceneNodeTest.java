package scene_handling;

import org.junit.Assert;
import org.junit.Test;
import net.whg.we.scene.SceneNode;

public class SceneNodeTest
{
	@Test
	public void setParent()
	{
		SceneNode node = new SceneNode();
		SceneNode parent = new SceneNode();

		node.setParent(parent);

		Assert.assertEquals(parent, node.getParent());
		Assert.assertEquals(1, parent.getChildCount());
		Assert.assertEquals(node, parent.getChild(0));
		Assert.assertEquals(0, node.getChildCount());
	}

	@Test
	public void setParent_ClearParent()
	{
		SceneNode node = new SceneNode();
		SceneNode parent = new SceneNode();
		node.setParent(parent);

		node.setParent(null);

		Assert.assertEquals(null, node.getParent());
		Assert.assertEquals(0, parent.getChildCount());
		Assert.assertEquals(0, node.getChildCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setParent_Cyclic_3Nodes()
	{
		SceneNode node = new SceneNode();
		SceneNode parent1 = new SceneNode();
		SceneNode parent2 = new SceneNode();

		node.setParent(parent1);
		parent1.setParent(parent2);
		parent2.setParent(node);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setParent_Cyclic_2Nodes()
	{
		SceneNode node = new SceneNode();
		SceneNode parent = new SceneNode();

		node.setParent(parent);
		parent.setParent(node);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setParent_Cyclic_1Node()
	{
		SceneNode node = new SceneNode();
		node.setParent(node);
	}

	@Test
	public void getName()
	{
		SceneNode node = new SceneNode();
		Assert.assertEquals("Empty Node", node.getName());

		node.setName("Test Node");
		Assert.assertEquals("Test Node", node.getName());
	}

	@Test
	public void setName_Null()
	{
		SceneNode node = new SceneNode();
		node.setName("Bob");

		node.setName(null);

		Assert.assertEquals("", node.getName());
	}

	@Test
	public void isEnabled()
	{
		SceneNode node = new SceneNode();
		Assert.assertTrue(node.isEnabled());

		node.setEnabled(false);
		Assert.assertFalse(node.isEnabled());
	}

	@Test
	public void isEnabledInHierarchy()
	{
		SceneNode node = new SceneNode();
		SceneNode parent1 = new SceneNode();
		SceneNode parent2 = new SceneNode();

		node.setParent(parent1);
		parent1.setParent(parent2);

		parent1.setEnabled(false);

		Assert.assertTrue(node.isEnabled());
		Assert.assertFalse(parent1.isEnabled());
		Assert.assertTrue(parent2.isEnabled());

		Assert.assertFalse(node.isEnabledInHierarchy());
		Assert.assertFalse(parent1.isEnabledInHierarchy());
		Assert.assertTrue(parent2.isEnabledInHierarchy());
	}
}
