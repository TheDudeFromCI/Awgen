package frameworks.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.scene.ISceneListener;
import net.whg.frameworks.scene.Scene;
import net.whg.frameworks.scene.SceneHierarchyChangedEvent;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.scene.Transform3D;

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
		SceneNode a = new SceneNode();
		SceneNode b = new SceneNode();
		SceneNode c = new SceneNode();
		SceneNode d = new SceneNode();
		SceneNode e = new SceneNode();

		a.addChild(b);
		b.addChild(c);
		c.addChild(d);
		d.addChild(e);

		c.setEnabled(false);

		Assert.assertTrue(a.isEnabled());
		Assert.assertTrue(b.isEnabled());
		Assert.assertFalse(c.isEnabled());
		Assert.assertTrue(d.isEnabled());
		Assert.assertTrue(e.isEnabled());

		Assert.assertTrue(a.isEnabledInHierarchy());
		Assert.assertTrue(b.isEnabledInHierarchy());
		Assert.assertFalse(c.isEnabledInHierarchy());
		Assert.assertFalse(d.isEnabledInHierarchy());
		Assert.assertFalse(e.isEnabledInHierarchy());
	}

	@Test
	public void getFullMatrix()
	{
		Matrix4f base = new Matrix4f();
		base.translate(10f, 30f, 50f);
		base.rotate(new Quaternionf().rotateLocalX((float) Math.toRadians(90f)));
		base.scale(4f);

		Matrix4f mat2 = new Matrix4f();
		mat2.translate(-5f, -17f, -5f);
		mat2.rotate(new Quaternionf().rotateLocalX((float) Math.toRadians(45f)));
		mat2.scale(0.5f);
		mat2 = new Matrix4f(base).mul(mat2);

		SceneNode top = new SceneNode();
		Transform3D transform = (Transform3D) top.getTransform();
		transform.setPosition(10f, 30f, 50f);
		transform.getRotation().rotateLocalX((float) Math.toRadians(90f));
		transform.setSize(4f);

		SceneNode child = new SceneNode();
		child.setParent(top);
		transform = (Transform3D) child.getTransform();
		transform.setPosition(-5f, -17f, -5f);
		transform.getRotation().rotateLocalX((float) Math.toRadians(45f));
		transform.setSize(0.5f);

		Matrix4f mat = new Matrix4f();
		child.getFullMatrix(mat);

		Assert.assertEquals(mat2, mat);
	}

	@Test
	public void addChild()
	{
		SceneNode node = new SceneNode();
		SceneNode child = new SceneNode();

		node.addChild(child);

		Assert.assertEquals(node, child.getParent());
		Assert.assertEquals(1, node.getChildCount());
		Assert.assertEquals(child, node.getChild(0));
		Assert.assertEquals(0, child.getChildCount());
	}

	@Test
	public void setParent_Null()
	{
		SceneNode node = new SceneNode();
		node.setParent(null);

		Assert.assertNull(node.getParent());
	}

	@Test
	public void setParent_Clear()
	{
		SceneNode node = new SceneNode();
		node.setParent(new SceneNode());

		node.setParent(null);

		Assert.assertNull(node.getParent());
	}

	@Test
	public void addChild_Null()
	{
		SceneNode node = new SceneNode();
		node.addChild(null);

		Assert.assertEquals(0, node.getChildCount());
	}

	@Test
	public void setTransform()
	{
		SceneNode node = new SceneNode();
		Transform3D t = new Transform3D();

		node.setTransform(t);

		Assert.assertEquals(t, node.getTransform());
	}

	@Test
	public void setTransform_Null()
	{
		SceneNode node = new SceneNode();
		node.setTransform(null);

		Assert.assertNotNull(node.getTransform());
	}

	@Test
	public void getLocalMatrix()
	{
		SceneNode node = new SceneNode();
		Transform3D t = (Transform3D) node.getTransform();
		t.setPosition(1, 2, 3);

		Matrix4f mat1 = new Matrix4f();
		t.getLocalMatrix(mat1);

		Matrix4f mat2 = new Matrix4f();
		node.getLocalMatrix(mat2);

		Assert.assertEquals(mat1, mat2);
	}

	@Test
	public void sceneHierarchyChangedEvent()
	{
		Scene scene = new Scene();
		ISceneListener listener = Mockito.mock(ISceneListener.class);
		scene.getEvent().addListener(listener);

		SceneNode node = new SceneNode();

		Mockito.doAnswer(a ->
		{
			SceneHierarchyChangedEvent e = a.getArgument(0);

			Assert.assertEquals(scene, e.getScene());
			Assert.assertEquals(node, e.getNode());
			Assert.assertEquals(scene.getRoot(), e.getNewParent());

			return null;
		}).when(listener).onSceneHierarchyChanged(Mockito.any());

		node.setParent(scene.getRoot());

		Mockito.verify(listener).onSceneHierarchyChanged(Mockito.any());
	}

	@Test(expected = IllegalStateException.class)
	public void setParentOfRootNode()
	{
		Scene scene = new Scene();

		SceneNode node = new SceneNode();
		scene.getRoot().setParent(node);
	}

	@Test
	public void getScene_Hierarchy()
	{
		Scene scene = new Scene();

		SceneNode node1 = new SceneNode();
		node1.setParent(scene.getRoot());

		SceneNode node2 = new SceneNode();
		node2.setParent(node1);

		SceneNode node3 = new SceneNode();

		Assert.assertEquals(scene, scene.getRoot().getScene());
		Assert.assertEquals(scene, node1.getScene());
		Assert.assertEquals(scene, node2.getScene());
		Assert.assertNull(node3.getScene());
	}

	@Test
	public void changeSceneHierarchy_TwoScenes()
	{
		Scene scene1 = new Scene();
		SceneNode node1 = new SceneNode();
		scene1.getRoot().addChild(node1);

		Scene scene2 = new Scene();
		SceneNode node2 = new SceneNode();
		scene2.getRoot().addChild(node2);

		ISceneListener listener1 = Mockito.mock(ISceneListener.class);
		scene1.getEvent().addListener(listener1);

		ISceneListener listener2 = Mockito.mock(ISceneListener.class);
		scene2.getEvent().addListener(listener2);

		node1.setParent(node2);

		Mockito.verify(listener1).onSceneHierarchyChanged(Mockito.any());
		Mockito.verify(listener2).onSceneHierarchyChanged(Mockito.any());

		Assert.assertEquals(scene2, node1.getScene());
		Assert.assertEquals(scene2, node2.getScene());
	}

	@Test
	public void changeSceneHierarchy_cancel()
	{
		Scene scene = new Scene();
		ISceneListener listener = Mockito.mock(ISceneListener.class);
		scene.getEvent().addListener(listener);

		Mockito.doAnswer(a ->
		{
			SceneHierarchyChangedEvent e = a.getArgument(0);
			e.setCanceled(true);
			return null;
		}).when(listener).onSceneHierarchyChanged(Mockito.any());

		SceneNode node = new SceneNode();
		boolean worked = node.setParent(scene.getRoot());

		Assert.assertFalse(worked);
		Assert.assertNull(node.getParent());
	}

	@Test
	public void changeSceneHierarchy_cancel_otherScene()
	{
		Scene scene = new Scene();
		SceneNode node1 = new SceneNode();
		scene.getRoot().addChild(node1);

		ISceneListener listener = Mockito.mock(ISceneListener.class);
		scene.getEvent().addListener(listener);

		Mockito.doAnswer(a ->
		{
			SceneHierarchyChangedEvent e = a.getArgument(0);
			e.setCanceled(true);
			return null;
		}).when(listener).onSceneHierarchyChanged(Mockito.any());

		Scene scene2 = new Scene();
		node1.setParent(scene2.getRoot());

		boolean worked = node1.setParent(scene.getRoot());

		Assert.assertFalse(worked);
		Assert.assertNotEquals(scene2.getRoot(), node1.getParent());
	}

	@Test(expected = IllegalStateException.class)
	public void setScene_NonRootNode()
	{
		SceneNode a = new SceneNode();
		SceneNode b = new SceneNode();
		a.addChild(b);

		Scene scene = new Scene();
		scene.setRoot(b);
	}

	@Test
	public void isRoot()
	{
		SceneNode a = new SceneNode();
		SceneNode b = new SceneNode();
		SceneNode c = new SceneNode();
		a.addChild(b);
		b.addChild(c);

		Assert.assertTrue(a.isRoot());
		Assert.assertFalse(b.isRoot());
		Assert.assertFalse(c.isRoot());

		Assert.assertEquals(a, a.getRootNode());
		Assert.assertEquals(a, b.getRootNode());
		Assert.assertEquals(a, c.getRootNode());
	}
}
