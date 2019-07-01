package frameworks.scene;

import org.junit.Assert;
import org.junit.Test;
import net.whg.frameworks.scene.Scene;
import net.whg.frameworks.scene.SceneNode;

public class SceneTest
{
	@Test
	public void init_scene()
	{
		Scene scene = new Scene();
		Assert.assertNotNull(scene.getRoot());
	}

	@Test
	public void set_root()
	{
		Scene scene = new Scene();

		SceneNode node = new SceneNode();
		scene.setRoot(node);

		Assert.assertEquals(node, scene.getRoot());
	}

	@Test
	public void set_root_null()
	{
		Scene scene = new Scene();
		SceneNode original = scene.getRoot();

		scene.setRoot(null);

		Assert.assertEquals(original, scene.getRoot());
	}
}
