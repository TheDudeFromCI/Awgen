package frameworks.scene;

import org.joml.Matrix4f;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import net.whg.frameworks.scene.BaseNodeFactory;
import net.whg.frameworks.scene.ITransform;
import net.whg.frameworks.scene.SceneLoader;
import net.whg.frameworks.scene.SceneNode;
import net.whg.frameworks.scene.Transform3D;
import net.whg.frameworks.scene.UnsupportedNodeType;
import net.whg.frameworks.util.ByteReader;
import net.whg.frameworks.util.ByteWriter;

public class SceneLoaderTest
{
	@Test
	public void save_load_SingleNode()
	{
		byte[] data = new byte[2048];

		// Save the node
		SceneNode oRoot = new SceneNode();
		{
			oRoot.setName("abcd");
			oRoot.setEnabled(false);
			((Transform3D) oRoot.getTransform()).setPosition(10, 20, 30);
			((Transform3D) oRoot.getTransform()).setSize(5);

			SceneLoader.saveScene(new ByteWriter(data), new BaseNodeFactory(), oRoot);
		}

		// Load the scenodee
		SceneNode nRoot = SceneLoader.loadScene(new ByteReader(data), new BaseNodeFactory());

		// Compare nodes
		{
			Assert.assertEquals(oRoot.getName(), nRoot.getName());
			Assert.assertEquals(oRoot.isEnabled(), nRoot.isEnabled());
			Assert.assertEquals(oRoot.getChildCount(), nRoot.getChildCount());

			Matrix4f mat1 = new Matrix4f();
			oRoot.getFullMatrix(mat1);
			Matrix4f mat2 = new Matrix4f();
			nRoot.getFullMatrix(mat2);
			Assert.assertEquals(mat1, mat2);
		}
	}

	@Test
	public void save_load_scene_full()
	{
		byte[] data = new byte[2048];

		// Save the scene
		SceneNode oRoot = new SceneNode();
		{
			oRoot.setName("abcd");
			oRoot.setEnabled(false);
			((Transform3D) oRoot.getTransform()).setPosition(10, 20, 30);

			SceneNode child1 = new SceneNode();
			child1.setParent(oRoot);
			child1.setName("1234");

			SceneNode child2 = new SceneNode();
			child2.setParent(oRoot);
			child2.setName("5678");

			SceneNode child1_1 = new SceneNode();
			child1_1.setParent(child1);
			child1_1.setName("derp");
			((Transform3D) child1_1.getTransform()).setSize(5);

			SceneLoader.saveScene(new ByteWriter(data), new BaseNodeFactory(), oRoot);
		}

		// Load the scene
		SceneNode nRoot = SceneLoader.loadScene(new ByteReader(data), new BaseNodeFactory());

		// Compare root nodes
		{
			Assert.assertEquals(oRoot.getName(), nRoot.getName());
			Assert.assertEquals(oRoot.isEnabled(), nRoot.isEnabled());
			Assert.assertEquals(oRoot.getChildCount(), nRoot.getChildCount());

			Matrix4f mat1 = new Matrix4f();
			oRoot.getFullMatrix(mat1);
			Matrix4f mat2 = new Matrix4f();
			nRoot.getFullMatrix(mat2);
			Assert.assertEquals(mat1, mat2);
		}

		// Compare child1 node
		{
			SceneNode a = oRoot.getChild(0);
			SceneNode b = nRoot.getChild(0);

			Assert.assertEquals(a.getName(), b.getName());
			Assert.assertEquals(a.getChildCount(), b.getChildCount());
		}

		// Compare child2 node
		{
			SceneNode a = oRoot.getChild(1);
			SceneNode b = nRoot.getChild(1);

			Assert.assertEquals(a.getName(), b.getName());
			Assert.assertEquals(a.getChildCount(), b.getChildCount());
		}

		// Compare child1_1 node
		{
			SceneNode a = oRoot.getChild(0).getChild(0);
			SceneNode b = nRoot.getChild(0).getChild(0);

			Assert.assertEquals(a.getName(), b.getName());
			Assert.assertEquals(a.isEnabled(), b.isEnabled());
			Assert.assertEquals(a.getChildCount(), b.getChildCount());

			Matrix4f mat1 = new Matrix4f();
			a.getFullMatrix(mat1);
			Matrix4f mat2 = new Matrix4f();
			b.getFullMatrix(mat2);
			Assert.assertEquals(mat1, mat2);
		}
	}

	@Test(expected = UnsupportedNodeType.class)
	public void BaseNodeFactory_Save_UnsupportedNodeType()
	{
		SceneNode node = Mockito.mock(SceneNode.class);
		Mockito.when(node.getNodeType()).thenReturn("unsupported.type");

		SceneLoader.saveScene(new ByteWriter(new byte[512]), new BaseNodeFactory(), node);
	}

	@Test(expected = UnsupportedNodeType.class)
	public void BaseNodeFactory_Load_UnsupportedNodeType()
	{
		byte[] data = new byte[512];

		SceneNode node = new SceneNode()
		{
			@Override
			public String getNodeType()
			{
				return "unsupported.type";
			}
		};
		SceneLoader.saveScene(new ByteWriter(data), new BaseNodeFactory()
		{
			@Override
			protected void saveNodeType(ByteWriter out, String type, SceneNode node)
			{
				if (type.equals("unsupported.type"))
					saveEmptySceneNode(out, node);
				else
					super.saveNodeType(out, type, node);
			}
		}, node);

		SceneLoader.loadScene(new ByteReader(data), new BaseNodeFactory());
	}

	@Test(expected = IllegalStateException.class)
	public void saveUnknownTransform()
	{
		ITransform transform = Mockito.mock(ITransform.class);

		SceneNode oRoot = new SceneNode();
		oRoot.setTransform(transform);

		SceneLoader.saveScene(new ByteWriter(new byte[512]), new BaseNodeFactory(), oRoot);
	}
}
